package fr.maxime38.interpreteur.renderers;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.maxime38.interpreteur.parsers.Node;
import fr.maxime38.interpreteur.styles.CSSRuleApplier;

public class HTMLRenderer {
	
	private static List<String> containers = Arrays.asList("document", "html", "body", "head", "div");
	private static CSSRuleApplier styleApplier;
	private static Node dom;
	
	public static JPanel render(Node dom) {
		HTMLRenderer.dom = dom;
		styleApplier = new CSSRuleApplier();
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Affichage vertical
	    traverseDOM(dom, panel/*, styleApplier*/);
	    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Ajoutez des marges
	    
	    //Provisoir
	    panel.setBackground(Color.LIGHT_GRAY);

	    
	    return panel;
	}


    private static void traverseDOM(Node node, JComponent parent/*, CSSRuleApplier styleApplier*/) {
        System.out.println("Processing node: " + node.getTagName());
        
        
        
        if (containers.contains(node.getTagName())) {
            for (Node child : node.getChildren()) {
                traverseDOM(child, parent/*, styleApplier*/);
            }
        } else if(node.getTagName().equals("style")) {
        	dom.setStyle(node.getStyle());
        } else if (node.getTagName().equals("h1")) {
            JLabel label = new JLabel(node.getTextContent());
            styleApplier.applyStyles(node, label);
            parent.add(label);
        } else if (node.getTagName().equals("p")) {
        	System.out.println("his style:" + node.getStyle());
            JLabel label = new JLabel(node.getTextContent());
            styleApplier.applyStyles(node, label);
            parent.add(label);
        } else {
            System.out.println("Unhandled tag: " + node.getTagName());
        }
    }

}