package fr.maxime38.interpreteur.parsers;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String tagName;
    private String textContent;
    private List<Attribute> attributes;
    private List<Node> children;
    private Node parent;

    public Node(String tagName) {
        this.tagName = tagName;
        this.textContent = "";
        this.attributes = new ArrayList<>();
        this.children = new ArrayList<>();
        this.parent = null;
    }

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addAttribute(String name, String value) {
        this.attributes.add(new Attribute(name, value));
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
    
    public String getTextContent() {
    	return textContent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
    
    public Node getParent() {
    	return parent;
    }

    @Override
    public String toString() {
        return "Node{\n\t\t" +
                "tagName='" + tagName + '\'' +
                ", textContent='" + textContent + '\'' +
                ", attributes=" + attributes +
                ", children=\n\t" + children +
                "}";
    }
}

class Attribute {
    private String name;
    private String value;

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + "=\"" + value + "\"";
    }
}