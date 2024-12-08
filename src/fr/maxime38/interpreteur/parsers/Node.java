package fr.maxime38.interpreteur.parsers;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String tagName;
    private String textContent;
    private List<NodeAttribute> attributes;
    private List<Node> children;
    private String style;
    private String parentStyle;
    private Node parent;

    public Node(String tagName) {
        this.tagName = tagName;
        this.textContent = "";
        this.attributes = new ArrayList<>();
        this.children = new ArrayList<>();
        this.parent = null;
        this.style = "";
        this.parentStyle = "";
    }

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
    }
    
    public List<Node> getChildren() {
    	return children;
    }

    public void addAttribute(String name, String value) {
        this.attributes.add(new NodeAttribute(name, value));
    }
    
    public List<NodeAttribute> getAttributes() {
    	return attributes;
    }
    
    public void setStyle(String style) {
    	this.style = style;
    }
    
    public String getStyle() {
    	return style;
    }
    
    public void addParentStyle(String style) {
    	this.parentStyle+=style;
    }
    
    public String getParentStyle() {
    	return parentStyle;
    }
    
    public void setAttributes(List<NodeAttribute> attributes) {
    	this.attributes = attributes;
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
    
    public String getTagName() {
    	return tagName;
    }
    
    /**
     * Tell if a node has attributes<br>
     *       @return boolean: True if the node contains any Attribute, False otherwise
     */
    public boolean hasAttributes() {
    	
    	return attributes.size() > 0;
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
    
    public String simplifiedString() {
    	return "Node{\n\t\t" +
                "tagName='" + tagName + '\'' +
                ", textContent='" + textContent + '\'' +
                ", attributes=" + attributes +
                "}";
    }
}
