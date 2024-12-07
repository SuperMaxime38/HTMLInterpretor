package fr.maxime38.interpreteur.parsers;

import java.util.HashMap;

public class Token {
	private TokenType type;
    private String value;
    private HashMap<String, String> attributes;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
        this.attributes = new HashMap<String, String>();
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
    
    public HashMap<String, String> getAttributes() {
    	return attributes;
    }
    
    public void setAttributes(HashMap<String, String> attributes) {
    	this.attributes = attributes;
    }
    
    public void addAttribute(String attribute, String value) {
    	this.attributes.put(attribute, value);
    }
    
    public void removeAttribute(String attribute) {
    	this.attributes.remove(attribute);
    }

    @Override
    public String toString() {
        return "Token{" + "type=" + type + ", value='" + value + '\'' + "attribute=" + attributes+ '}';
    }
}
