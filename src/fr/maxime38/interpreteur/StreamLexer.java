package fr.maxime38.interpreteur;

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
            return new Token(TokenType.EOF, "");
        }

        if (currentChar == '<') {
            consume(); // Avance dans le flux
            if (currentChar == '/') {
                consume();
                String tagName = readTagName();
                skipWhitespace();
                expect('>');
                return new Token(TokenType.CLOSING_TAG, tagName);
            } else {
                String tagName = readTagName();
                return handleOpeningTag(tagName);
            }
        }

        // Contenu texte ou contenu brut
        if (Character.isLetterOrDigit(currentChar) || currentChar == ' ') {
            return new Token(TokenType.TEXT, readText());
        }

        throw new RuntimeException("Caractère non reconnu : " + (char) currentChar);
    }

    private void consume() throws IOException {
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

        // Lire les attributs ou détecter une balise auto-fermante
        while (currentChar != -1 && currentChar != '>' && currentChar != '/') {
            buffer.append((char) currentChar);
            consume();
        }

        boolean selfClosing = false;
        if (currentChar == '/') {
            selfClosing = true;
            consume();
        }

        expect('>'); // Fin de balise

        String value = tagName + (buffer.length() > 0 ? " " + buffer.toString().trim() : "");
        return new Token(selfClosing ? TokenType.SELF_CLOSING_TAG : TokenType.OPENING_TAG, value);
    }
}
