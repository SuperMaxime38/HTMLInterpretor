package fr.maxime38.interpreteur.css;

import java.util.ArrayList;
import java.util.List;

import fr.maxime38.interpreteur.css.utils.CSSDeclaration;
import fr.maxime38.interpreteur.css.utils.CSSRule;

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
        // Ignorez les commentaires, passez au jeton suivant si nous rencontrons un commentaire
        while (currentToken.getType() == CSSTokenType.COMMENT) {
            currentToken = lexer.nextToken();
        }

        // Vérifier si nous avons un jeton IDENTIFIER pour le sélecteur
        if (currentToken.getType() == CSSTokenType.EOF) {
            throw new RuntimeException("Erreur : Sélecteur attendu mais trouvé EOF");
        }

        if (currentToken.getType() != CSSTokenType.IDENTIFIER) {
            throw new RuntimeException("Erreur : Sélecteur attendu mais trouvé " + currentToken.getType());
        }

        String selector = currentToken.getValue();
        currentToken = lexer.nextToken();  // Consommer le sélecteur

        return selector;
    }




    // Parser les déclarations CSS (propriétés et valeurs)
    private List<CSSDeclaration> parseDeclarations() {
        List<CSSDeclaration> declarations = new ArrayList<>();

        while (currentToken.getType() != CSSTokenType.CLOSE_BRACE) {
            String property = parseProperty();
            consumeToken(CSSTokenType.COLON);  // Consommer ':'
            String value = parseValue();
            consumeToken(CSSTokenType.SEMICOLON);  // Consommer ';'

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
        String value = currentToken.getValue();

        // Vérifier si nous avons un nombre et une unité
        if (currentToken.getType() == CSSTokenType.NUMBER) {
            value = currentToken.getValue();
            currentToken = lexer.nextToken();  // Consommer le nombre

            // Vérifier si une unité suit le nombre
            if (currentToken.getType() == CSSTokenType.UNIT) {
                value += currentToken.getValue();  // Ajouter l'unité à la valeur
                currentToken = lexer.nextToken();  // Consommer l'unité
            }
        }

        // Vérifier si la valeur est un identifiant ou une autre valeur valide
        if (currentToken.getType() == CSSTokenType.IDENTIFIER ||
            currentToken.getType() == CSSTokenType.STRING) {
            value = currentToken.getValue();
            currentToken = lexer.nextToken();  // Consommer la valeur
        }

        return value;
    }

    // Consommer un jeton spécifique
    private void consumeToken(CSSTokenType expectedType) {
        if (currentToken.getType() != expectedType) {
            throw new RuntimeException("Erreur : Attendu " + expectedType + " mais trouvé " + currentToken.getType());
        }
        currentToken = lexer.nextToken();
    }
}

