package fr.maxime38.interpreteur.parsers;

public class Token {
	private TokenType type;
    private String value;
    private String attributes;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
        this.attributes = null;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
    
    public String getAttributes() {
    	return attributes;
    }
    
    public void setAttributes(String attributes) {
    	this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", value='" + value + '\'' + '}';
    }
}
