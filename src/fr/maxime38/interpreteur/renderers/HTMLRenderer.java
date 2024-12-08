package fr.maxime38.interpreteur.renderers;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.maxime38.interpreteur.parsers.Node;
import fr.maxime38.interpreteur.parsers.css.CSSLexer;
import fr.maxime38.interpreteur.parsers.css.CSSParser2;
import fr.maxime38.interpreteur.styles.CSSRuleApplier;

public class HTMLRenderer {
	
	private static List<String> containers = Arrays.asList("document", "html", "body", "head", "div");
	private static CSSRuleApplier styleApplier;
	private static Node dom;
	private static HashMap<String, HashMap<String, String>> properties;
	private static CSSLexer lexer;
	private static CSSParser2 parser;
	
	public static JPanel render(Node dom) {
		HTMLRenderer.dom = dom;
		styleApplier = new CSSRuleApplier();
		
		properties = new HashMap<String, HashMap<String, String>>();
		
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
        
        //Ajouter un systeme qui apply les style des balises globales ici
        
        
        
        if (containers.contains(node.getTagName())) {
            for (Node child : node.getChildren()) {
                traverseDOM(child, parent/*, styleApplier*/);
            }
        } else if(node.getTagName().equals("style")) {
        	dom.setStyle(node.getStyle());
        } else if (node.getTagName().equals("h1")) {
            JLabel label = new JLabel(node.getTextContent());
            //label.setForeground(Color.BLUE);
            styleApplier.applyStyles(node, label);
            parent.add(label);
        } else if (node.getTagName().equals("p")) {
        	//System.out.println("his style:" + node.getStyle());
            JLabel label = new JLabel(node.getTextContent());
            parent.add(label);

            //Application du CSS du noeud
            lexer = new CSSLexer(node.getStyle());
            properties = new CSSParser2(lexer, false).parse();
            applyStyle(label);
        } else {
            System.out.println("Unhandled tag: " + node.getTagName());
        }
    }
    
    private static void applyStyle(JComponent component) {
    	if(properties.containsKey("this")) {
    		HashMap<String, String> props = properties.get("this");
    		for(String property : props.keySet()) {
    			switch(property) {
    			case "color" -> component.setForeground(Color.decode(props.get(property)));
    			case "font-family" -> component.setFont(new Font(props.get(property), Font.PLAIN, 14));
    			case "text-align" -> {
    				if (component instanceof JLabel label) {
    					if (props.get(property).equals("center")) {
    						label.setHorizontalAlignment(SwingConstants.CENTER);
                      }
                  }
              }
              // Ajoutez d'autres styles...
    			
    			}
    		}
    	}
    }

}