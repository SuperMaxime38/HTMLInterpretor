package fr.maxime38.interpreteur.parsers;

import java.io.IOException;
import java.util.Map;

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
            System.out.println("(Opening Tag) Looking : "+tagName);
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
                    return node;
                } else if (currentToken.getType() == TokenType.OPENING_TAG) {
                	Node child = parseElement();
                	child.setParent(node);
                	child.addParentStyle(node.getStyle());
                    node.addChild(child);
                } else if (currentToken.getType() == TokenType.SELF_CLOSING_TAG) {
                	Node child = parseElement();
                	child.setParent(node);
                	child.addParentStyle(node.getStyle());
                    node.addChild(child);
                } else if(currentToken.getType() == TokenType.RAW_CONTENT) {
                	Node child = parseElement();
                	child.setParent(node);
                	child.addParentStyle(node.getStyle());
                    node.addChild(child);
                	
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
        
        if(currentToken.getType() == TokenType.RAW_CONTENT) {
        	Node node = new Node(currentToken.getValue());
            if (currentToken.getAttributes() != null) {
            	System.out.println("ATTRIBUTS : "+currentToken.getAttributes());
                parseAttributes(node, currentToken.getAttributes());
            }
            currentToken = lexer.nextToken();
            return node;
        	
        }

        throw new RuntimeException("Unexpected token: " + currentToken);
    }


//    private Node parseElement() throws IOException{
//    	//System.out.println("HAndling: "+currentToken);
//        if (currentToken.getType() == TokenType.OPENING_TAG) {
//            String tagName = currentToken.getValue();
//            System.out.println("(Opening Tag) Looking : "+tagName);
//            Node node = new Node(tagName);
//
//            // Gérer les attributs si présents
//            if (currentToken.getAttributes() != null) {
//                parseAttributes(node, currentToken.getAttributes());
//            }
//
//            // Passer au prochain token
//            currentToken = lexer.nextToken();
//
//            // Gérer les enfants jusqu'à la balise fermante correspondante
//            while (currentToken.getType() != TokenType.CLOSING_TAG && !currentToken.getValue().equals(tagName)) {
//                if (currentToken.getType() == TokenType.OPENING_TAG) {
//                	System.out.println("INSIDE LOOP OPENING");
//                	Node child = parseElement();
//                	child.setParent(node);
//                	child.addParentStyle(node.getStyle());
//                    node.addChild(child);
//                } else if (currentToken.getType() == TokenType.TEXT) {
//                	System.out.println("TEXT: "+currentToken + " current Node:"+node.simplifiedString());
//                    node.setTextContent((node.getTextContent() + " " + currentToken.getValue()).trim());
//                } else if (currentToken.getType() == TokenType.SELF_CLOSING_TAG) {
//                	Node child = parseElement();
//                	child.setParent(node);
//                	child.addParentStyle(node.getStyle());
//                    node.addChild(child);
//                } else if(currentToken.getType() == TokenType.RAW_CONTENT) {
//                	Node child = parseElement();
//                	child.setParent(node);
//                	child.addParentStyle(node.getStyle());
//                    node.addChild(child);
//                	
//                } else if (currentToken.getType() == TokenType.EOF) {
//                    break;
//                }
//                currentToken = lexer.nextToken(); // Avancer dans les tokens
//                System.out.println("1new current:"+currentToken.getType() + " for node"+node.simplifiedString());
//            }
//            
//            // Passer au prochain token après la balise fermante
//            currentToken = lexer.nextToken();
//            System.out.println("2new current:"+currentToken.getType()+ " for node"+node.simplifiedString());
//            return node;
//        }
//
//        if (currentToken.getType() == TokenType.SELF_CLOSING_TAG) {
//            Node node = new Node(currentToken.getValue());
//            if (currentToken.getAttributes() != null) {
//                parseAttributes(node, currentToken.getAttributes());
//            }
//            currentToken = lexer.nextToken();
//            return node;
//        }
//        
//        if(currentToken.getType() == TokenType.RAW_CONTENT) {
//        	Node node = new Node(currentToken.getValue());
//            if (currentToken.getAttributes() != null) {
//                parseAttributes(node, currentToken.getAttributes());
//            }
//            currentToken = lexer.nextToken();
//            return node;
//        	
//        }
//        
//        if(currentToken.getType() == TokenType.TEXT) {
//        	System.out.println("called yayyyyy");
//        	Node node = new Node(currentToken.getValue());
//            if (currentToken.getAttributes() != null) {
//                parseAttributes(node, currentToken.getAttributes());
//            }
//            currentToken = lexer.nextToken();
//            return node;
//        	
//        }
//
//        throw new RuntimeException("Unexpected token: " + currentToken);
//    }

    private void parseAttributes(Node node, Map<String, String> attributes) {
    	
    	for(String key: attributes.keySet()) {
    		if(key.equals("style") || key.equals(" style")) {
    			node.setStyle(attributes.get(key));
    		} else {
    			node.addAttribute(key, attributes.get(key));
    		}
    		
    	}
    }
}