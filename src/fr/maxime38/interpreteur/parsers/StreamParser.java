package fr.maxime38.interpreteur.parsers;

import java.io.IOException;
import java.util.Map;

public class StreamParser {
    private StreamLexer lexer;
    private Token currentToken;
    private Node dom;

    public StreamParser(StreamLexer lexer) throws IOException {
    	this.dom = null;
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
    }

    public Node parse() throws IOException{
        Node root = new Node("document");
        dom = root;
        while (currentToken.getType() != TokenType.EOF) {
            System.out.println("Parsing token: " + currentToken); // Debug
            root.addChild(parseElement());
        }
        System.out.println("Finished parsing. Reached EOF."); // Debug
        return root;
    }
    
    private Node parseElement() throws IOException {
    	if(currentToken.getType().equals(TokenType.OPENING_TAG)) {
    		String tagName = currentToken.getValue();
    		System.out.println("(Opening Tag) Looking : "+tagName);
    		Node node = new Node(tagName);
    		
    		// Gérer les attributs si présents
    		if (currentToken.getAttributes() != null) {
    			parseAttributes(node, currentToken.getAttributes());
    		}

    		// Passer au prochain token
    		currentToken = lexer.nextToken();
    		
    		while(!currentToken.getType().equals(TokenType.CLOSING_TAG)) {
    			switch(currentToken.getType()) {
    			case TokenType.OPENING_TAG: //balise imbriquée
    				
    				Node child = parseElement();
                	child.setParent(node);
                	child.addParentStyle(node.getStyle());
                	node.addChild(child);
    				break;
    			
    			case TokenType.TEXT:
    				node.setTextContent((node.getTextContent() + " " + currentToken.getValue()).trim());
    				break;
    				
    			case SELF_CLOSING_TAG:
    				Node child2 = parseElement();
                	child2.setParent(node);
                	child2.addParentStyle(node.getStyle());
                    node.addChild(child2);
                    break;
                    
    			case RAW_CONTENT: // <style> or <script>
    				Node child3 = parseElement();
                	child3.setParent(node);
                	child3.addParentStyle(node.getStyle());
                    node.addChild(child3);
    				break;
    				
				default:
					currentToken = lexer.nextToken();
					return node;
    			}
    			
    			currentToken = lexer.nextToken();
    		}
    		
    		//On arrive a un closing tag
    		// Passer au prochain token après la balise fermante
    		//currentToken = lexer.nextToken();
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
    		if(currentToken.getValue().equals("style")) {
    			System.out.println("HEY I HAVE BEEN CALLED");
    			dom.setStyle(node.getStyle());
    			dom.addAttribute("globalCSS", "true");
    		}
    		currentToken = lexer.nextToken();
    		return node;
      	
    	}
    	
    	throw new RuntimeException("Unexpected token: " + currentToken);
    }
    

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