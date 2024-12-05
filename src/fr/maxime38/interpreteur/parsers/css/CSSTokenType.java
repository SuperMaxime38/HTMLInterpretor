package fr.maxime38.interpreteur.parsers.css;

public enum CSSTokenType {
    // Délimiteurs
    OPEN_BRACE,       // {
    CLOSE_BRACE,      // }
    COLON,            // :
    SEMICOLON,        // ;
    COMMA,            // ,
    OPEN_PAREN,       // (
    CLOSE_PAREN,      // )

    // Identifiants et mots-clés
    IDENTIFIER,       // Noms génériques (propriétés, valeurs, sélecteurs)

    // Types spécifiques
    NUMBER,           // 12, 1.5
    UNIT,             // px, em, %
    STRING,           // "Helvetica"

    // Sélecteurs et combinateurs
    HASH,             // #id
    CLASS,            // .class
    STAR,             // *
    GREATER_THAN,     // >
    PLUS,             // +
    TILDE,            // ~

    // Fonctions CSS
    FUNCTION,         // rgb(, url(

    // Commentaires
    COMMENT,          // /* ... */

    // Fin de fichier
    EOF               // Fin du flux
}

