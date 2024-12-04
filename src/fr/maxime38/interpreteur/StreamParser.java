package fr.maxime38.interpreteur;

import java.io.IOException;

public class StreamParser {
    private StreamLexer lexer;
    private Token currentToken;

    public StreamParser(StreamLexer lexer) throws IOException {
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
    }

    public Node parse() throws IOException{
        Node root = new Node("document");
        while (currentToken.getType() != TokenType.EOF) {
            System.out.println("Parsing token: " + currentToken); // Debug
            root.addChild(parseElement());
        }
        System.out.println("Finished parsing. Reached EOF."); // Debug
        return root;
    }


    private Node parseElement() throws IOException{
        if (currentToken.getType() == TokenType.OPENING_TAG) {
            String tagName = currentToken.getValue();
            Node node = new Node(tagName);

            // Gérer les attributs si présents
            if (currentToken.getAttributes() != null) {
                parseAttributes(node, currentToken.getAttributes());
            }

            // Passer au prochain token
            currentToken = lexer.nextToken();

            // Gérer les enfants jusqu'à la balise fermante correspondante
            while (currentToken.getType() != TokenType.CLOSING_TAG || !currentToken.getValue().equals(tagName)) {
                if (currentToken.getType() == TokenType.TEXT) {
                    node.setTextContent((node.getTextContent() + " " + currentToken.getValue()).trim());
                } else if (currentToken.getType() == TokenType.OPENING_TAG) {
                    node.addChild(parseElement());
                } else if (currentToken.getType() == TokenType.SELF_CLOSING_TAG) {
                    node.addChild(parseElement());
                } else if (currentToken.getType() == TokenType.EOF) {
                    break;
                }
                currentToken = lexer.nextToken(); // Avancer dans les tokens
            }

            // Passer au prochain token après la balise fermante
            currentToken = lexer.nextToken();
            return node;
        }

        if (currentToken.getType() == TokenType.SELF_CLOSING_TAG) {
            Node node = new Node(currentToken.getValue());
            if (currentToken.getAttributes() != null) {
                parseAttributes(node, currentToken.getAttributes());
            }
            currentToken = lexer.nextToken();
            return node;
        }

        throw new RuntimeException("Unexpected token: " + currentToken);
    }




    private void parseAttributes(Node node, String attributesText) {
        String[] parts = attributesText.split("\\s+");
        for (String part : parts) {
            if (part.contains("=")) {
                String[] keyValue = part.split("=", 2);
                String key = keyValue[0];
                String value = keyValue[1].replaceAll("\"", ""); // Retirer les guillemets
                node.addAttribute(key, value);
            }
        }
    }
}