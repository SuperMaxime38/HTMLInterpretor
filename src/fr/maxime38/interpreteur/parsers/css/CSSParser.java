package fr.maxime38.interpreteur.parsers.css;

import java.util.ArrayList;
import java.util.List;

import fr.maxime38.interpreteur.parsers.css.utils.CSSDeclaration;
import fr.maxime38.interpreteur.parsers.css.utils.CSSRule;

public class CSSParser {
    private CSSLexer lexer;
    private CSSToken currentToken;

    public CSSParser(CSSLexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
    }

    // Méthode pour parser une règle CSS (sélecteur + déclarations)
    public List<CSSRule> parse() {
        List<CSSRule> rules = new ArrayList<>();
        
        while (currentToken.getType() != CSSTokenType.EOF) {
        	while (currentToken.getType() == CSSTokenType.COMMENT) {
        		currentToken = lexer.nextToken();
        	}
        	if(currentToken.getType() != CSSTokenType.EOF) {
        		rules.add(parseRule());  // Analyser chaque règle
        	}
           
        }
        
        // Si EOF est atteint, cela signifie qu'il n'y a plus de règles CSS
        return rules;
    }


    // Parser une règle (sélecteur + déclarations)
    private CSSRule parseRule() {

        String selector = parseSelector();  // Analyser le sélecteur CSS
        consumeToken(CSSTokenType.OPEN_BRACE);  // Consommer le '{'

        List<CSSDeclaration> declarations = parseDeclarations();  // Analyser les déclarations

        consumeToken(CSSTokenType.CLOSE_BRACE);  // Consommer le '}'

        return new CSSRule(selector, declarations);  // Retourner la règle CSS analysée
    }


    // Parser un sélecteur CSS
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
                throw new RuntimeException("Erreur : Sélecteur non terminé, attendu '{' mais trouvé EOF");
            }
        }

        // Supprimer l'espace final inutile
        return selector.toString().trim();
    }





    // Parser les déclarations CSS (propriétés et valeurs)
    private List<CSSDeclaration> parseDeclarations() {
        List<CSSDeclaration> declarations = new ArrayList<>();

        while (currentToken.getType() != CSSTokenType.CLOSE_BRACE) {
            String property = parseProperty();
            consumeToken(CSSTokenType.COLON);  // Consommer ':'
            String value = parseValue();

            // Vérifier si un point-virgule est présent ou si nous sommes à la fin du bloc
            if (currentToken.getType() == CSSTokenType.SEMICOLON) {
                consumeToken(CSSTokenType.SEMICOLON);  // Consommer ';'
            } else if (currentToken.getType() != CSSTokenType.CLOSE_BRACE) {
                throw new RuntimeException("Erreur : Attendu SEMICOLON ou CLOSE_BRACE mais trouvé " + currentToken.getType());
            }

            declarations.add(new CSSDeclaration(property, value));
        }

        return declarations;
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

 // Parser une valeur CSS (par exemple, "#fff" ou "16px")
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
                throw new RuntimeException("Erreur : Valeur CSS non terminée avant EOF");
            }
        }

        // Supprimer l'espace final inutile
        return value.toString().trim();
    }





    // Consommer un jeton spécifique
    private void consumeToken(CSSTokenType expectedType) {
        if (currentToken.getType() != expectedType) {
            throw new RuntimeException("Erreur : Attendu " + expectedType + " mais trouvé " + currentToken.getType());
        }
        currentToken = lexer.nextToken();
    }
}

