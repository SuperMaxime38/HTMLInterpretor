package fr.maxime38.interpreteur.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class StreamLexer {
    private BufferedReader reader;
    private int currentChar; // Caractère courant
    private StringBuilder buffer; // Pour accumuler les données si nécessaire
    private StringBuilder buffer2; // Pour ne pas overwrite le premier

    public StreamLexer(Reader input) throws IOException {
        this.reader = new BufferedReader(input);
        this.currentChar = reader.read();
        this.buffer = new StringBuilder();
        this.buffer2 = new StringBuilder();
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
                    Token token = new Token(TokenType.RAW_CONTENT, tagName);
                    if(tagName.equals("style")) token.addAttribute("style", rawContent);
                    if(tagName.equals("script")) token.addAttribute("script", rawContent);
                    //System.out.println("nextToken(): Generated -> " + token);
                    return token;
                }
                
                //Si ya des attributs dans le tagName
                
                
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
        buffer2.setLength(0);
        while (currentChar != -1 && (Character.isLetterOrDigit(currentChar) || currentChar == '-')) {
            buffer2.append((char) currentChar);
            consume();
        }
        return buffer2.toString();
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
        Token token = new Token(selfClosing ? TokenType.SELF_CLOSING_TAG : TokenType.OPENING_TAG, tagName);
        
        // TO HANDLE THE ATTRIBITE DO SOMETHING COMPLICATED
        
        String attr = (buffer.length() > 0 ? " " + buffer.toString().trim() : "");
        
        String[] attrVal = attr.split("\"");
        
        byte counter=0;
    	for(int i = 0; i < (attrVal.length-3); i+=1) {
    		token.addAttribute(attrVal[i+counter].split("=")[0], attrVal[(i+1)+counter]);
    		counter++;
    	}
        
        
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
                        break; // Sortir une fois la balise fermante trouvée
                    }
                }
                buffer.append('<'); // Si ce n'est pas une balise fermante, conserver '<'
            } else {
            	if(((char) currentChar) != ">".toCharArray()[0]) {
                    buffer.append((char) currentChar);
            	}
                consume();
            }
            //System.out.println(buffer.toString());
        }
        return buffer.toString().trim();
    }



}
