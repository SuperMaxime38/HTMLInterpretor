package fr.maxime38.interpreteur.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class StreamLexer {
    private BufferedReader reader;
    private int currentChar; // Caractère courant
    private StringBuilder buffer; // Pour accumuler les données si nécessaire

    public StreamLexer(Reader input) throws IOException {
        this.reader = new BufferedReader(input);
        this.currentChar = reader.read();
        this.buffer = new StringBuilder();
    }

    public Token nextToken() throws IOException {
        skipWhitespace();

        if (currentChar == -1) {
            System.out.println("Token EOF generated");
            return new Token(TokenType.EOF, "");
        }

        if (currentChar == '<') {
            consume();
            if (currentChar == '/') {
                consume();
                String tagName = readTagName();
                skipWhitespace();
                expect('>');
                Token token = new Token(TokenType.CLOSING_TAG, tagName);
                //System.out.println("nextToken(): Generated -> " + token);
                return token;
            } else {
                String tagName = readTagName();
                if (tagName.equals("style") || tagName.equals("script")) {
                    String rawContent = readRawContent(tagName);
                    Token token = new Token(TokenType.RAW_CONTENT, rawContent);
                    //System.out.println("nextToken(): Generated -> " + token);
                    return token;
                }
                Token token = handleOpeningTag(tagName);
                //System.out.println("nextToken(): Generated -> " + token);
                return token;
            }
        }

        if (Character.isLetterOrDigit(currentChar) || currentChar == ' ') {
            String text = readText();
            Token token = new Token(TokenType.TEXT, text);
            //System.out.println("nextToken(): Generated -> " + token);
            return token;
        }

        throw new RuntimeException("Caractère non reconnu : " + (char) currentChar);
    }



    private void consume() throws IOException {
    	//System.out.println("consume(): Current char -> " + (char) currentChar);

        currentChar = reader.read();
    }

    private void expect(char expected) throws IOException {
        if (currentChar != expected) {
            throw new RuntimeException("Erreur : attendu '" + expected + "' mais trouvé '" + (char) currentChar + "'");
        }
        consume();
    }

    private String readTagName() throws IOException {
        buffer.setLength(0);
        while (currentChar != -1 && (Character.isLetterOrDigit(currentChar) || currentChar == '-')) {
            buffer.append((char) currentChar);
            consume();
        }
        //System.out.println("TagName: " + buffer.toString());
        return buffer.toString();
    }

    private String readText() throws IOException {
        buffer.setLength(0);
        while (currentChar != -1 && currentChar != '<') {
            buffer.append((char) currentChar);
            consume();
        }
        return buffer.toString().trim();
    }

    private void skipWhitespace() throws IOException {
        while (currentChar != -1 && Character.isWhitespace(currentChar)) {
            consume();
        }
    }

    private Token handleOpeningTag(String tagName) throws IOException {
        buffer.setLength(0);

        while (currentChar != -1 && currentChar != '>' && currentChar != '/') {
            buffer.append((char) currentChar);
            consume();
        }

        boolean selfClosing = false;
        if (currentChar == '/') {
            selfClosing = true;
            consume();
        }

        expect('>');
        String value = tagName + (buffer.length() > 0 ? " " + buffer.toString().trim() : "");
        Token token = new Token(selfClosing ? TokenType.SELF_CLOSING_TAG : TokenType.OPENING_TAG, value);
        //System.out.println("handleOpeningTag(): Generated -> " + token);
        return token;
    }


    
    private String readRawContent(String tagName) throws IOException {
        buffer.setLength(0);
        while (currentChar != -1) {
            if (currentChar == '<') {
                consume();
                if (currentChar == '/') {
                    consume();
                    String closingTag = readTagName();
                    if (closingTag.equals(tagName)) {
                        skipWhitespace();
                        expect('>');
                        break; // Sortir de la boucle une fois la balise fermante trouvée
                    }
                }
                buffer.append('<'); // Conserve '<' si ce n'est pas une balise fermante
            } else {
                buffer.append((char) currentChar);
                consume();
            }
        }
        //System.out.println("RawContent: " + buffer.toString());
        return buffer.toString().trim();
    }


}
