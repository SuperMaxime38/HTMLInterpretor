package fr.maxime38.interpreteur.css;

public class CSSLexer {
    private final String input;
    private int position;
    private boolean isLastTokenNumber;

    public CSSLexer(String input) {
        this.input = input;
        this.position = 0;
        this.isLastTokenNumber = false;
    }

    // Méthode principale pour récupérer le prochain jeton
    public CSSToken nextToken() {
    	//System.out.println("Position : " + position + ", caractère actuel : " + input.charAt(position));

        skipWhitespace();

        if (position >= input.length()) {
            return new CSSToken(CSSTokenType.EOF, "");
        }

        char current = input.charAt(position);

        // Vérifier les commentaires CSS
        if (current == '/' && peekNextChar() == '*') {
            return consumeComment();
        }
        
        if(isLastTokenNumber) {
        	isLastTokenNumber = false;
        	return consumeUnit();
        }

        // Détecter les délimiteurs simples
        if (current == '{') return consumeToken(CSSTokenType.OPEN_BRACE);
        if (current == '}') return consumeToken(CSSTokenType.CLOSE_BRACE);
        if (current == ':') return consumeToken(CSSTokenType.COLON);
        if (current == ';') return consumeToken(CSSTokenType.SEMICOLON);
        if (current == ',') return consumeToken(CSSTokenType.COMMA);
        if (current == '(') return consumeToken(CSSTokenType.OPEN_PAREN);
        if (current == ')') return consumeToken(CSSTokenType.CLOSE_PAREN);

        // Détecter les identifiants
        if (Character.isLetter(current) || current == '#' || current == '.' || current == '-') {
            return consumeIdentifier();
        }

        // Détecter les nombres
        if (Character.isDigit(current)) {
            return consumeNumber();
        }

        // Détecter les chaînes de caractères
        if (current == '"') {
            return consumeString();
        }

        // Détecter les symboles CSS spécifiques
        if (current == '*') return consumeToken(CSSTokenType.STAR);
        if (current == '>') return consumeToken(CSSTokenType.GREATER_THAN);
        if (current == '+') return consumeToken(CSSTokenType.PLUS);
        if (current == '~') return consumeToken(CSSTokenType.TILDE);

        // Si aucun cas n'est satisfait, avance d'un caractère pour éviter une boucle infinie
        throw new RuntimeException("Caractère inattendu : '" + current + "' à la position " + position++);
    }


    // Consommer un jeton et avancer d'un caractère
    private CSSToken consumeToken(CSSTokenType type) {
        char current = input.charAt(position);
        position++;
        return new CSSToken(type, Character.toString(current));
    }

    // Consommer un identifiant (noms de sélecteurs, propriétés, etc.)
    private CSSToken consumeIdentifier() {
        int start = position;

        char current = input.charAt(position);

        // Vérifier si c'est un hash (#)
        if (current == '#') {
            position++; // Consomme le #

            // Vérifier si c'est une valeur hexadécimale
            if (position < input.length() && isHexCharacter(input.charAt(position))) {
                return consumeHexValue(start);
            }

            // Sinon, retourner un jeton HASH
            return new CSSToken(CSSTokenType.HASH, "#");
        }

        // Vérifier si c'est un identifiant standard (propriétés, sélecteurs)
        if (Character.isLetter(current) || current == '.' || current == '-') {
            while (position < input.length() &&
                   (Character.isLetterOrDigit(input.charAt(position)) || 
                    input.charAt(position) == '-' || 
                    input.charAt(position) == '_')) {
                position++;
            }
            String value = input.substring(start, position);
            return new CSSToken(CSSTokenType.IDENTIFIER, value);
        }

        // Si aucun identifiant valide, lancer une exception
        throw new RuntimeException("Erreur : identifiant invalide à la position " + position);
    }

    // Méthode pour consommer une valeur hexadécimale
    private CSSToken consumeHexValue(int start) {
        while (position < input.length() && isHexCharacter(input.charAt(position))) {
            position++;
        }

        String value = input.substring(start, position);
        return new CSSToken(CSSTokenType.IDENTIFIER, value); // Utiliser HASH si approprié
    }

    // Vérifier si un caractère est hexadécimal
    private boolean isHexCharacter(char c) {
        return Character.isDigit(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }



    // Consommer un nombre (valeurs numériques)
    private CSSToken consumeNumber() {
        int start = position;

        // Consommer la partie numérique
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            position++;
        }

        // Gérer les nombres à virgule flottante
        if (position < input.length() && input.charAt(position) == '.') {
            position++;
            while (position < input.length() && Character.isDigit(input.charAt(position))) {
                position++;
            }
        }

        // Extraire la valeur numérique
        String numberValue = input.substring(start, position);

        // Retourner le jeton NUMÉRIQUE d'abord
        CSSToken numberToken = new CSSToken(CSSTokenType.NUMBER, numberValue);

        // Vérifier s'il y a une unité immédiatement après
        if (position < input.length() && isUnitStart(input.charAt(position))) {
            //CSSToken unitToken = consumeUnit();  // Consommer et retourner l'unité séparément
            // Retourner d'abord le nombre, puis l'unité
        	isLastTokenNumber = true; // Soluce random que j'ai trouvé (ca marche)
            return numberToken;
        }

        // Retourner uniquement le nombre si aucune unité n'est détectée
        return numberToken;
    }

    // Consommer l'unité (par exemple px, em, %)
    private CSSToken consumeUnit() {
        int start = position;
        while (position < input.length() && Character.isLetter(input.charAt(position))) {
            position++;
        }
        String unitValue = input.substring(start, position);
        return new CSSToken(CSSTokenType.UNIT, unitValue);  // Retourner un jeton UNIT pour l'unité
    }




    // Vérifier si un caractère peut commencer une unité
    private boolean isUnitStart(char c) {
        return Character.isLetter(c);
    }


    // Consommer une chaîne de caractères (valeurs entre guillemets)
    private CSSToken consumeString() {
        position++;  // Consomme le guillemet initial
        int start = position;
        while (position < input.length() && input.charAt(position) != '"') {
            position++;
        }

        if (position < input.length()) {
            String value = input.substring(start, position);
            position++;  // Consomme le guillemet final
            return new CSSToken(CSSTokenType.STRING, value);
        }

        throw new RuntimeException("Erreur : chaîne de caractères non fermée");
    }

    // Consommer un commentaire CSS
    private CSSToken consumeComment() {
        position += 2;  // Consomme '/*'
        int start = position;
        while (position < input.length() - 1 && !(input.charAt(position) == '*' && input.charAt(position + 1) == '/')) {
            position++;
        }

        if (position < input.length() - 1) {
            String value = input.substring(start, position);
            position += 2;  // Consomme '*/'
            return new CSSToken(CSSTokenType.COMMENT, value);
        }

        throw new RuntimeException("Erreur : commentaire non fermé");
    }

    // Sauter les espaces et sauts de ligne
    private void skipWhitespace() {
        while (position < input.length() && Character.isWhitespace(input.charAt(position))) {
            position++;
        }
    }

    // Regarder le prochain caractère sans avancer la position
    private char peekNextChar() {
        if (position + 1 < input.length()) {
            return input.charAt(position + 1);
        }
        return '\0';  // Fin de chaîne
    }
}
