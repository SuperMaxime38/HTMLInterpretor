package fr.maxime38.interpreteur.css.utils;

public class CSSDeclaration {
    private String property;
    private String value;

    public CSSDeclaration(String property, String value) {
        this.property = property;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return property + ": " + value;
    }
}
