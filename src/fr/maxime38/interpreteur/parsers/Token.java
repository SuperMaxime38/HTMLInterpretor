package fr.maxime38.interpreteur.parsers;

import java.util.List;

public class Token {
	private TokenType type;
    private String value;
    private List<String> attributes;

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
    
    public List<String> getAttributes() {
    	return attributes;
    }
    
    public void setAttributes(List<String> attributes) {
    	this.attributes = attributes;
    }
    
    public void addAttribute(String attribute) {
    	this.attributes.add(attribute);
    }
    
    public void removeAttribute(String attribute) {
    	this.attributes.remove(attribute);
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", value='" + value + '\'' + "attribute=" + attributes+ '}';
    }
}
