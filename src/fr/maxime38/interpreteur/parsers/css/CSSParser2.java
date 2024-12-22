package fr.maxime38.interpreteur.parsers.css;

import java.util.HashMap;

public class CSSParser2 {
	
	private CSSLexer lexer;
    private CSSToken currentToken;
    private HashMap<String, String> property;
    private HashMap<String, HashMap<String, String>> properties;

    private boolean isGlobal;
    
    public CSSParser2(CSSLexer lexer, boolean isGlobal) {
    	this.isGlobal = isGlobal;
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
        this.property = new HashMap<String, String>();
        this.properties = new HashMap<String, HashMap<String, String>>();
    }
    
    public HashMap<String, HashMap<String, String>> parse() {
    	if(isGlobal) {
    		parseGlobal();
    	} else {
    		parseNormal();
    	}
    	return properties;
    }
    
    public void parseNormal() {
    	while(currentToken.getType() != CSSTokenType.EOF) {
    		while (currentToken.getType() == CSSTokenType.COMMENT) {
        		currentToken = lexer.nextToken();
        	}
    		if(currentToken.getType() != CSSTokenType.EOF) {
    			property.clear();
    	        parseDeclarationsNormal();  // Analyser les déclarations
    		}
    		System.out.println("CURRENT:"+currentToken.toString());
    		
    	}
    }
    
    public void parseGlobal() {
    	while(currentToken.getType() != CSSTokenType.EOF) {
    		while (currentToken.getType() == CSSTokenType.COMMENT) {
        		currentToken = lexer.nextToken();
        	}
    		if(currentToken.getType() != CSSTokenType.EOF) {
    			property.clear();
    			parseRule();
    		}
    		
    	}
    }
    
    private void parseRule() {

        String selector = parseSelector();  // Analyser le sélecteur CSS
        consumeToken(CSSTokenType.OPEN_BRACE);  // Consommer le '{'

        parseDeclarations(selector);  // Analyser les déclarations

        consumeToken(CSSTokenType.CLOSE_BRACE);  // Consommer le '}'

        
    }
    
    private void parseDeclarationsNormal() {
    	while(!currentToken.getType().equals(CSSTokenType.SEMICOLON) || !currentToken.getType().equals(CSSTokenType.EOF) ) {
    		System.out.println("currentToken="+currentToken);
    		if(!currentToken.getType().equals(CSSTokenType.SEMICOLON) && !currentToken.getType().equals(CSSTokenType.EOF)) { // for some reason i need that
    		String property = parseProperty();
    		consumeToken(CSSTokenType.COLON);
    		String value = parseValue();
    		
    		// Vérifier si un point-virgule est présent ou si nous sommes à la fin du bloc
            if (currentToken.getType() == CSSTokenType.SEMICOLON) {
                consumeToken(CSSTokenType.SEMICOLON);  // Consommer ';'
            } else if (currentToken.getType() != CSSTokenType.SEMICOLON) {
            	if(currentToken.getType().equals(CSSTokenType.EOF)) break;
                throw new RuntimeException("Erreur : Attendu SEMICOLON mais trouvé " + currentToken.getType());
            }
            
            this.property.put(property, value);
            properties.put("this", this.property);
    		} else {
    			return;
    		}
    	}
    }
    
    private void parseDeclarations(String selector) {
    	while(currentToken.getType() != CSSTokenType.CLOSE_BRACE || currentToken.getType() != CSSTokenType.EOF) {
    		if(!currentToken.getType().equals(CSSTokenType.CLOSE_BRACE)) {
    		String property = parseProperty();
    		consumeToken(CSSTokenType.COLON);
    		String value = parseValue();
    		
    		// Vérifier si un point-virgule est présent ou si nous sommes à la fin du bloc
            if (currentToken.getType() == CSSTokenType.SEMICOLON) {
                consumeToken(CSSTokenType.SEMICOLON);  // Consommer ';'
            } else if (currentToken.getType() != CSSTokenType.CLOSE_BRACE) {
                throw new RuntimeException("Erreur : Attendu SEMICOLON ou CLOSE_BRACE mais trouvé " + currentToken.getType());
            }
            
            this.property.put(property, value);
            properties.put(selector, this.property);
    		} else {
    			return;
    		}
    		
    	}
    }
    
    // Parser une propriété CSS (par exemple, "color")
    private String parseProperty() {
        if (currentToken.getType() != CSSTokenType.IDENTIFIER) {
            throw new RuntimeException("Erreur : Propriété attendue mais trouvé " + currentToken.getType());
        }

        String property = currentToken.getValue();
        currentToken = lexer.nextToken();  // Consommer la propriété
        return property;
    }
    
    private String parseValue() {
        StringBuilder value = new StringBuilder();

        // Consommer tous les tokens valides pour une valeur CSS
        while (currentToken.getType() != CSSTokenType.SEMICOLON &&
               currentToken.getType() != CSSTokenType.CLOSE_BRACE) {
            
            // Ajouter la valeur actuelle au résultat
            value.append(currentToken.getValue());

            // Ajouter un espace pour séparer les composants de la valeur
            value.append(" ");

            // Avancer au prochain token
            currentToken = lexer.nextToken();

            // Vérifier si nous atteignons la fin du fichier
            if (currentToken.getType() == CSSTokenType.EOF) {
                //throw new RuntimeException("Erreur : Valeur CSS non terminée avant EOF");
            	break;
            }
        }

        // Supprimer l'espace final inutile
        return value.toString().trim();
    }
    
    private String parseSelector() {
        StringBuilder selector = new StringBuilder();

        // Construire le sélecteur jusqu'à rencontrer une ouverture de bloc '{'
        while (currentToken.getType() != CSSTokenType.OPEN_BRACE) {
            if (currentToken.getType() == CSSTokenType.COMMENT) {
                currentToken = lexer.nextToken(); // Ignorer les commentaires
                continue;
            }

            // Ajouter le token actuel au sélecteur
            selector.append(currentToken.getValue());
            selector.append(" "); // Ajouter un espace entre les parties du sélecteur
            currentToken = lexer.nextToken();

            // Vérifier si on atteint la fin du fichier
            if (currentToken.getType() == CSSTokenType.EOF) {
                break;
            }
        }

        // Supprimer l'espace final inutile
        return selector.toString().trim();
    }
    
 // Consommer un jeton spécifique
    private void consumeToken(CSSTokenType expectedType) {
        if (currentToken.getType() != expectedType) {
            throw new RuntimeException("Erreur : Attendu " + expectedType + " mais trouvé " + currentToken.getType());
        }
        currentToken = lexer.nextToken();
    }
}
