package fr.maxime38.interpreteur.parsers;

import java.util.regex.Pattern;

public class Lexer {
    private String input;
    private int position;

    // Modèle pour extraire les attributs
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile("\\s*([a-zA-Z_:][a-zA-Z0-9_:\\-]*)\\s*(=\\s*\"([^\"]*)\")?");

    public Lexer(String input) {
        this.input = input.trim();
        this.position = 0;
    }

    public Token nextToken() {
        skipWhitespace();

        if (position >= input.length()) {
            return new Token(TokenType.EOF, "");
        }

        char currentChar = input.charAt(position);

        if (currentChar == '<') {
            position++;
            if (position < input.length() && input.charAt(position) == '/') {
                position++;
                String tagName = readTagName();
                skipWhitespace();
                expect('>'); // Balise fermante détectée
                return new Token(TokenType.CLOSING_TAG, tagName);
            } else {
                return parseOpeningTag(); // Appel ici pour gérer les balises ouvrantes
            }
        }

        // Texte classique
        if (Character.isLetterOrDigit(currentChar) || currentChar == ' ') {
            return new Token(TokenType.TEXT, readText());
        }

        throw new RuntimeException("Caractère non reconnu : " + currentChar);
    }

    private void skipWhitespace() {
        while (position < input.length() && Character.isWhitespace(input.charAt(position))) {
            position++;
        }
    }

    private String readTagName() {
        StringBuilder tagName = new StringBuilder();
        while (position < input.length() && Character.isLetterOrDigit(input.charAt(position))) {
            tagName.append(input.charAt(position));
            position++;
        }
        return tagName.toString();
    }

    private String readText() {
        StringBuilder text = new StringBuilder();
        while (position < input.length() && input.charAt(position) != '<') {
            text.append(input.charAt(position));
            position++;
        }
        return text.toString().trim();
    }

    private void expect(char expected) {
        if (position >= input.length() || input.charAt(position) != expected) {
            throw new RuntimeException("Erreur : attendu '" + expected + "' mais trouvé '" + (position >= input.length() ? "EOF" : input.charAt(position)) + "'");
        }
        position++;
    }

    private Token parseOpeningTag() {
        String tagName = readTagName();
        StringBuilder attributes = new StringBuilder();

        // Lire les attributs jusqu'à la fin de la balise
        while (position < input.length() && input.charAt(position) != '>') {
            if (input.startsWith("/>", position)) {
                position += 2; // Auto-fermante
                return new Token(TokenType.SELF_CLOSING_TAG, tagName);
            } else {
                attributes.append(input.charAt(position));
                position++;
            }
        }
        position++; // Consommer le '>'

        // Extraction des attributs avec la regex
        String attributesText = attributes.toString().trim();
        StringBuilder parsedAttributes = new StringBuilder();
        var matcher = ATTRIBUTE_PATTERN.matcher(attributesText);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(3) != null ? matcher.group(3) : "";
            parsedAttributes.append(key).append("=").append("\"").append(value).append("\" ").trimToSize();
        }

        Token token = new Token(TokenType.OPENING_TAG, tagName);
        if (parsedAttributes.length() > 0) {
            token.addAttribute(parsedAttributes.toString().trim());
        }

        return token;
    }


}
