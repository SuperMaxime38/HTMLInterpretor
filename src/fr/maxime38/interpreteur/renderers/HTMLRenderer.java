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
			
			case "aliceblue": return Color.decode("#f0f8ff");
			case "antiquewhite": return Color.decode("#faebd7");
			case "aqua": return Color.decode("#00FFFF");
			case "aquamarine": return Color.decode("#7fffd4");
			case "azure": return Color.decode("#f0ffff");
			case "beige": return Color.decode("#f5f5dc");
			case "bisque": return Color.decode("#ffe4c4");
			case "blanchedalmond": return Color.decode("#ffebcd");
			case "blue": return Color.blue;
			case "blueviolet": return Color.decode("#8a2be2");
			case "brown": return Color.decode("#a52a2a");
			case "burlywood": return Color.decode("#deb887");
			case "cadetblue": return Color.decode("#5f9ea0");
			case "chartreuse": return Color.decode("#7fff00");
			case "chocolate": return Color.decode("#d2691e");
			case "coral": return Color.decode("#ff7f50");
			case "cornflowerblue": return Color.decode("#6495ed");
			case "cornsilk": return Color.decode("#fff8dc");
			case "crimson": return Color.decode("#dc143c");
			case "cyan": return Color.decode("#00ffff");
			case "darkblue": return Color.decode("#00008b");
			case "darkcyan": return Color.decode("#008b8b");
			case "darkgoldenrod": return Color.decode("#b8860b");
			case "darkgray": return Color.decode("#a9a9a9");
			case "darkgrey": return Color.decode("#006400");
			case "darkgreen": return Color.decode("#a9a9a9");
			case "darkkhaki": return Color.decode("#bdb76b");
			case "darkmagenta": return Color.decode("#8b008b");
			case "darkolivegreen": return Color.decode("#556b2f");
			case "darkorange": return Color.decode("#ff8c00");
			case "darkorchid": return Color.decode("#9932cc");
			case "darkred": return Color.decode("#8b0000");
			case "darksalmon": return Color.decode("#e9967a");
			case "darkseagreen": return Color.decode("#8fbc8f");
			case "darkslateblue": return Color.decode("#483d8b");
			case "darkslategray": return Color.decode("#2f4f4f");
			case "darkslategrey": return Color.decode("#2f4f4f");
			case "darkturquoise": return Color.decode("#00ced1");
			case "darkviolet": return Color.decode("#9400d3");
			case "deeppink": return Color.decode("#ff1493");
			case "deepskyblue": return Color.decode("#00bfff");
			case "dimgray": return Color.decode("#696969");
			case "dimgrey": return Color.decode("#696969");
			case "dodgerblue": return Color.decode("#1e90ff");
			case "firebrick": return Color.decode("#b22222");
			case "floralwhite": return Color.decode("#fffaf0");
			case "forestgreen": return Color.decode("#228b22");
			case "gainsboro": return Color.decode("#dcdcdc");
			case "ghostwhite": return Color.decode("#f8f8ff");
			case "gold": return Color.decode("#ffd700");
			case "goldenrod": return Color.decode("#daa520");
			case "greenyellow": return Color.decode("#adff2f");
			case "honeydew": return Color.decode("#f0fff0");
			
			case "hotpink": return Color.decode("#ff69b4");
			case "indianred": return Color.decode("#cd5c5c");
			case "indigo": return Color.decode("#4b0082");
			case "ivory": return Color.decode("#fffff0");
			case "khaki": return Color.decode("#f0e68c");
			case "lavender": return Color.decode("#e6e6fa");
			case "lavenderblush": return Color.decode("#fff0f5");
			case "lawngreen": return Color.decode("#7cfc00");
			case "lemonchiffon": return Color.decode("#fffacd");
			case "lightblue": return Color.decode("#add8e6");
			case "lightcoral": return Color.decode("#f08080");
			case "lightcyan": return Color.decode("#e0ffff");
			case "lightgoldenrodyellow": return Color.decode("#fafad2");
			case "lightgray": return Color.decode("#d3d3d3");
			case "lightgreen": return Color.decode("#90ee90");
			case "lightgrey": return Color.decode("#d3d3d3");
			case "lightpink": return Color.decode("#ffb6c1");
			case "lightsalmon": return Color.decode("#ffa07a");
			case "lightseagreen": return Color.decode("#20b2aa");
			case "lightskyblue": return Color.decode("#87cefa");
			case "lightslategray": return Color.decode("#778899");
			case "lightslategrey": return Color.decode("#778899");
			case "lightsteelblue": return Color.decode("#b0c4de");
			case "lightyellow": return Color.decode("#ffffe0");
			
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			case "greenyellow": return Color.decode("#00FFFF");
			
			
			default: return Color.black;
			}
		}
    }

}