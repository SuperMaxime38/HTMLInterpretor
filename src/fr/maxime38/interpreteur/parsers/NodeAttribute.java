package fr.maxime38.interpreteur.parsers;

public class NodeAttribute {
	private String name;
    private String value;

    public NodeAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
    
    public String getValue() {
    	return value;
    }
    @Override
    public String toString() {
        return name + "=\"" + value + "\"";
    }
}
