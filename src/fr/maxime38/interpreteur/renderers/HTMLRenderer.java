package fr.maxime38.interpreteur.renderers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fr.maxime38.interpreteur.parsers.Node;
import fr.maxime38.interpreteur.parsers.NodeAttribute;
import fr.maxime38.interpreteur.parsers.css.CSSLexer;
import fr.maxime38.interpreteur.parsers.css.CSSParser2;

public class HTMLRenderer {
	
	private static List<String> fullscreenStyles = Arrays.asList("document", "html", "body");
	private static List<String> containers = Arrays.asList("document", "html", "body", "head", "div");
	private static HashMap<String, HashMap<String, String>> properties;
	private static CSSLexer lexer;
	private static JPanel root;
	
	public static JPanel render(Node dom) {
		
		properties = new HashMap<String, HashMap<String, String>>();
		
	    JPanel panel = new JPanel();
	    root = panel;
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Affichage vertical
	    traverseDOM(dom, panel/*, styleApplier*/);
	    panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Ajoutez des marges
	    
	    return panel;
	}


	@SuppressWarnings("deprecation")
	private static void traverseDOM(Node node, JComponent parent) {
		System.out.println("Parcourt: "+node.getTagName());
	    if (containers.contains(node.getTagName())) {
	        JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Vertical par défaut

	        lexer = new CSSLexer(node.getStyle());
	        
	        System.out.println("TEST: " + node.getTagName() + " attributes " + node.getAttributes().toString() + " style : " + node.getStyle());
	        System.out.println("contains globalCSS : " + containsGlobalCSS(node));
	        
	        properties = new CSSParser2(lexer,containsGlobalCSS(node)).parse();
	        if(fullscreenStyles.contains(node.getTagName())) { //Maybe useless who knows
	        	applyStyle(parent);
	        }
	        applyStyle(panel);

	        for (Node child : node.getChildren()) {
	            traverseDOM(child, panel);
	        }

	        panel.setMaximumSize(new Dimension(panel.getMaximumSize().width, panel.getPreferredSize().height));
	        panel.revalidate();
	        panel.repaint();
	        //System.out.println("panel: "+node.getTagName()+"PANEL SIZE:"+panel.getSize().toString()+ ", PREFERRED:" +panel.getPreferredSize().toString());
	        parent.add(panel);
	    } else if (node.getTagName().equals("h1") || node.getTagName().equals("p")) {
	        JLabel label = new JLabel(node.getTextContent());
	        parent.add(label);

//	        lexer = new CSSLexer(node.getParentStyle());
//	        
//	        properties = new CSSParser2(lexer,containsGlobalCSS(node.getParent())).parse();
//	        applyStyle(label);

	        lexer = new CSSLexer(node.getStyle());
	        properties = new CSSParser2(lexer,false).parse();
	        applyStyle(label);
	    }else if (node.getTagName().equals("img")) {
	        String source = getSource(node);
	        if (source == null || source.isEmpty()) {
	            System.err.println("Balise <img> sans attribut 'src' ou avec un 'src' vide.");
	            return;
	        }

	        BufferedImage myPicture;
            File inputFile = new File(source);
            

			try {
				if(inputFile.exists()) {
					myPicture = ImageIO.read(inputFile);
				} else {
					URL url = new URL(source);
					myPicture = ImageIO.read(url);
				}
				JLabel picture = new JLabel(new ImageIcon(myPicture));
				parent.add(picture);
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            

	    } else {
	        System.out.println("Unhandled tag: " + node.getTagName());
	    }
	}
	
	private static boolean containsGlobalCSS(Node node) {
		for(NodeAttribute attribute : node.getAttributes()) {
			if(attribute.getName().equals("globalCSS")) return true;
		}
		return false;
	}
	
	private static String getSource(Node node) {
		for(NodeAttribute attribute : node.getAttributes()) {
			if(attribute.getName().equals("src")) {
				return attribute.getValue();
			}
		}
		
		return "";
	}
	
    private static void applyStyle(JComponent component) {
        if (properties.containsKey("this")) {
            HashMap<String, String> props = properties.get("this");
            for (String property : props.keySet()) {
                switch (property) {
                    case "color":
                        component.setForeground(getColor(props.get(property)));
                        break;
                    case "background-color":
                        component.setBackground(getColor(props.get(property)));
                        if (component instanceof JPanel panel) {
                        	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Disposition verticale
                        	panel.setOpaque(true); // Assure que la couleur de fond est visible

                        }
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
                    case "display":
                        if (component instanceof JPanel panel) {
                            switch (props.get(property)) {
                                case "inline":
                                    panel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Affichage horizontal
                                    break;
                                default:
                                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Disposition verticale par défaut
                                    break;
                            }
                        }
                        break;
                        
                    case "width":
                        int width = Integer.parseInt(props.get(property).replace("px", ""));
                        component.setPreferredSize(new Dimension(width, component.getPreferredSize().height));
                        break;
                    case "height":
                        int height = Integer.parseInt(props.get(property).replace("px", ""));
                        component.setPreferredSize(new Dimension(component.getPreferredSize().width, height));
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
			case "green": return Color.green;
			case "grey": return Color.gray;
			case "gray": return Color.gray;
			case "fuchsia": return Color.decode("#FF00FF");
			
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
			case "lime": return Color.decode("#00ff00");
			case "limegreen": return Color.decode("#32cd32");
			case "linen": return Color.decode("#faf0e6");
			case "magenta": return Color.decode("#ff00ff");
			case "maroon": return Color.decode("#800000");
			case "mediumaquamarine": return Color.decode("#66cdaa");
			case "mediumblue": return Color.decode("#0000cd");
			case "mediumorchid": return Color.decode("#ba55d3");
			case "mediumpurple": return Color.decode("#9370db");
			case "mediumseagreen": return Color.decode("#3cb371");
			case "mediumslateblue": return Color.decode("#7b68ee");
			case "mediumspringgreen": return Color.decode("#00fa9a");
			case "mediumturquoise": return Color.decode("#48d1cc");
			case "mediumvioletred": return Color.decode("#c71585");
			case "midnightblue": return Color.decode("#191970");
			case "mintcream": return Color.decode("#f5fffa");
			case "mistyrose": return Color.decode("#ffe4e1");
			case "moccasin": return Color.decode("#ffe4b5");
			case "navajowhite": return Color.decode("#ffdead");
			case "navy": return Color.decode("#000080");
			case "oldlace": return Color.decode("#fdf5e6");
			case "olive": return Color.decode("#808000");
			case "olivedrab": return Color.decode("#6b8e23");
			case "orange": return Color.decode("#ffa500");
			case "orangered": return Color.decode("#ff4500");
			case "orchid": return Color.decode("#da70d6");
			case "palegoldenrod": return Color.decode("#eee8aa");
			case "palegreen": return Color.decode("#98fb98");
			case "paleturquoise": return Color.decode("#afeeee");
			case "palevioletred": return Color.decode("#db7093");
			case "papayawhip": return Color.decode("#ffefd5");
			case "peachpuff": return Color.decode("#ffdab9");
			case "peru": return Color.decode("#cd853f");
			case "pink": return Color.decode("#ffc0cb");
			case "plum": return Color.decode("#dda0dd");
			case "powderblue": return Color.decode("#b0e0e6");
			case "purple": return Color.decode("#800080");
			case "rebeccapurple": return Color.decode("#663399");
			case "red": return Color.decode("#ff0000");
			case "rosybrown": return Color.decode("#bc8f8f");
			case "royalblue": return Color.decode("#4169e1");
			case "saddlebrown": return Color.decode("#8b4513");
			case "salmon": return Color.decode("#fa8072");
			case "sandybrown": return Color.decode("#f4a460");
			case "seagreen": return Color.decode("#2e8b57");
			case "seashell": return Color.decode("#fff5ee");
			case "sienna": return Color.decode("#a0522d");
			case "silver": return Color.decode("#c0c0c0");
			case "skyblue": return Color.decode("#87ceeb");
			case "slateblue": return Color.decode("#6a5acd");
			case "slategray": return Color.decode("#708090");
			case "slategrey": return Color.decode("#708090");
			case "snow": return Color.decode("#fffafa");
			case "springgreen": return Color.decode("#00ff7f");
			case "steelblue": return Color.decode("#4682b4");
			case "tan": return Color.decode("#d2b48c");
			case "teal": return Color.decode("#008080");
			case "thistle": return Color.decode("#d8bfd8");
			case "tomato": return Color.decode("#ff6347");
			
			case "transparent": return new Color(0,0,0,0); // Transparent color 
			
			case "turquoise": return Color.decode("#40e0d0");
			case "violet": return Color.decode("#ee82ee");
			case "wheat": return Color.decode("#f5deb3");
			case "white": return Color.decode("#ffffff");
			case "whitesmoke": return Color.decode("#f5f5f5");
			case "yellow": return Color.decode("#ffff00");
			case "yellowgreen": return Color.decode("#9acd32");
			
			
			default: return Color.black;
			}
		}
    }

}