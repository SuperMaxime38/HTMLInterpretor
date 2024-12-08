package fr.maxime38.interpreteur.utils;

import java.util.ArrayList;
import java.util.List;

import fr.maxime38.interpreteur.parsers.Node;
import fr.maxime38.interpreteur.parsers.NodeAttribute;

public class DOMUtils {
    public static List<String> extractStyles(Node dom) {
        List<String> styles = new ArrayList<>();
        traverseDOM(dom, "");
        return styles;
    }

	public static void traverseDOM(Node node, String styles) {
    	
    	System.out.println(node.simplifiedString());
    	
    	//PLACEHOLDER (ne sert à rien)
        if (node.getTagName().equals("style")) {
        	System.out.println("It's a style balise");
            //styles.add(node.getTextContent()); // Ajouter le contenu du nœud <style>
        }
        if(!styles.equals("")) {
        	node.addParentStyle(styles);
        }
        
        if(!node.getStyle().equals("")) {
        	styles+=node.getStyle();
        }
        
        if(node.hasAttributes()) {
        	System.out.println("it has attributes");
        	for(NodeAttribute attribute : node.getAttributes()) {
        		if(attribute.getName().equals("style")) {
        			styles+=attribute.getValue();
        			System.out.println("Value of attribute: " + attribute.getValue());
        		}
        	}
        }

        for (Node child : node.getChildren()) {
            traverseDOM(child, styles); // Parcourir récursivement les enfants
        }
    }
}
