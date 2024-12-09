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
	    
	    return panel;
	}


    private static void traverseDOM(Node node, JComponent parent/*, CSSRuleApplier styleApplier*/) {
        System.out.println("Processing node: " + node.getTagName());
        
        //Ajouter un systeme qui apply les style des balises globales ici
        
        
        
        if (containers.contains(node.getTagName())) {
        	lexer = new CSSLexer(node.getStyle());
            properties = new CSSParser2(lexer, false).parse();
            applyStyle(parent);
            for (Node child : node.getChildren()) {
            	child.addParentStyle(node.getStyle());
                traverseDOM(child, parent/*, styleApplier*/);
            }
        } else if(node.getTagName().equals("style")) {
        	dom.setStyle(node.getStyle());
        } else if (node.getTagName().equals("h1")) {
            JLabel label = new JLabel(node.getTextContent());
            
            //Application style parent
            lexer = new CSSLexer(node.getParentStyle());
            properties = new CSSParser2(lexer, false).parse();
            applyStyle(label);
            
            
            //Application du CSS du noeud
            lexer = new CSSLexer(node.getStyle());
            properties = new CSSParser2(lexer, false).parse();
            applyStyle(label);
            parent.add(label);
            System.out.println("color is now:"+label.getForeground());
        } else if (node.getTagName().equals("p")) {
        	//System.out.println("his style:" + node.getStyle());
            JLabel label = new JLabel(node.getTextContent());
            parent.add(label);
            

            //Application style parent
            lexer = new CSSLexer(node.getParentStyle());
            properties = new CSSParser2(lexer, false).parse();
            applyStyle(label);

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
    		System.out.println("JUSQU'ICI TOUT VA BIEN");
    		HashMap<String, String> props = properties.get("this");
    		for(String property : props.keySet()) {
    			System.out.println("PROPERTTY:"+property);
    			switch(property) {
    			case "color":
    				System.out.println("color called");
    				Color c = getColor(props.get(property));
    				
    				System.out.println("COLOR:"+c.toString());
    				
    				component.setForeground(c);
    				break;
    			case "background-color":
    				System.out.println("bg-col called | component:"+component.toString());
    				component.setBackground(getColor(props.get(property)));
    				break;
    			case "font-family":
    				component.setFont(new Font(props.get(property), Font.PLAIN, 14));
    				break;
    			case "text-align":
    				if (component instanceof JLabel label) {
    					if (props.get(property).equals("center")) {
    						label.setHorizontalAlignment(SwingConstants.CENTER);
    					}
    				}

    				break;
              // Ajoutez d'autres styles...
    			
    			}
    		}
    	}
    }
    
    public static Color getColor(String color) {
    	try {
			return Color.decode(color);
		} catch(NumberFormatException e) {
			switch(color) {
			case "white": return Color.white;
			case "red": return Color.red;
			case "blue": return Color.blue;
			case "green": return Color.green;
			case "yellow": return Color.yellow;
			case "purple": return Color.decode("#800080");
			case "grey": return Color.gray;
			case "gray": return Color.gray;
			case "pink": return Color.pink;
			case "orange": return Color.orange;
			case "silver": return Color.decode("#C0C0C0");
			case "maroon": return Color.decode("#800000");
			case "fuchsia": return Color.decode("#FF00FF");
			case "lime": return Color.decode("#00FF00");
			case "olive": return Color.decode("#808000");
			case "navy": return Color.decode("#000080");
			case "teal": return Color.decode("#008080");
			case "aqua": return Color.decode("#00FFFF");
			
			case "aliceblue": return Color.decode("#00FFFF");
			case "antiquewhite": return Color.decode("#00FFFF");
			case "aquamarine": return Color.decode("#00FFFF");
			case "azure": return Color.decode("#00FFFF");
			case "beige": return Color.decode("#00FFFF");
			case "bisque": return Color.decode("#00FFFF");
			case "blanchedalmond": return Color.decode("#00FFFF");
			case "blueviolet": return Color.decode("#00FFFF");
			case "brown": return Color.decode("#00FFFF");
			case "burlywood": return Color.decode("#00FFFF");
			case "cadetblue": return Color.decode("#00FFFF");
			case "chartreuse": return Color.decode("#00FFFF");
			case "chocolate": return Color.decode("#00FFFF");
			case "coral": return Color.decode("#00FFFF");
			case "cornflowerblue": return Color.decode("#00FFFF");
			case "cornsilk": return Color.decode("#00FFFF");
			case "crimson": return Color.decode("#00FFFF");
			case "cyan": return Color.decode("#00FFFF");
			case "darkblue": return Color.decode("#00FFFF");
			case "darkcyan": return Color.decode("#00FFFF");
			case "darkgoldenrod": return Color.decode("#00FFFF");
			case "darkgray": return Color.decode("#00FFFF");
			case "darkgrey": return Color.decode("#00FFFF");
			case "darkgreen": return Color.decode("#00FFFF");
			case "darkkhaki": return Color.decode("#00FFFF");
			case "darkmagenta": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			case "aqua": return Color.decode("#00FFFF");
			
			
			default: return Color.black;
			}
		}
    }

}