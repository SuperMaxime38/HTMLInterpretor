package fr.maxime38.interpreteur.css;

public class CSSToken {
    private final CSSTokenType type;
    private final String value;

    public CSSToken(CSSTokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public CSSTokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type + ": " + value;
    }
}
