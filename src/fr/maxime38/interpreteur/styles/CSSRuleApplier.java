package fr.maxime38.interpreteur.styles;

import javax.swing.JComponent;

import fr.maxime38.interpreteur.parsers.Node;
import fr.maxime38.interpreteur.parsers.css.CSSLexer;
import fr.maxime38.interpreteur.parsers.css.CSSParser2;

public class CSSRuleApplier {
//    private List<CSSRule> cssRules;
//
//    public CSSRuleApplier(List<CSSRule> cssRules) {
//        this.cssRules = cssRules;
//    }

//    public void applyStyles(JComponent component, String tagName) {
//        for (CSSRule rule : cssRules) {
//            if (rule.getSelector().equals(tagName)) {
//                for (var declaration : rule.getDeclarations()) {
//                	System.out.println("DECLARATION:" + declaration);
//                    switch (declaration.getProperty()) {
//                        case "color" -> component.setForeground(Color.decode(declaration.getValue()));
//                        case "font-family" -> component.setFont(new Font(declaration.getValue(), Font.PLAIN, 14));
//                        case "text-align" -> {
//                            if (component instanceof JLabel label) {
//                                if (declaration.getValue().equals("center")) {
//                                    label.setHorizontalAlignment(SwingConstants.CENTER);
//                                }
//                            }
//                        }
//                        // Ajoutez d'autres styles...
//                    }
//                }
//            }
//        }
//    }
    
    public void applyStyles(Node node, JComponent component) {
    	
    	
    	//Apply parent style
    	
    	String parentStyles = node.getParentStyle();
    	if(!parentStyles.equals("")) {
    		CSSLexer parentLexer = new CSSLexer(parentStyles);
    	CSSParser2 parentParser = new CSSParser2(parentLexer, false);
    	var parentCssRules = parentParser.parse();
    	
    	//component = apply(component, parentCssRules);
    	//System.out.println("PARENT STYLE: " + parentCssRules);
    	}
    	
    	
    	//Then own style
    	
    	String styles = node.getStyle();
    	if(!styles.equals("")) {
    		//System.out.println("styles : " + styles);
    	CSSLexer lexer = new CSSLexer(styles);
    	CSSParser2 parser = new CSSParser2(lexer, false);
    	var cssRules = parser.parse();
    	
    	//return apply(component, cssRules);
    	//System.out.println("OWN STYLE: " + cssRules);
    	}
    	
    	
    }
    
//    private JComponent apply(JComponent component, List<CSSRule> cssRules) {
//    	System.out.println("rules:" + cssRules.toString());
//    	for(CSSRule rule : cssRules) {
//    		for(var declaration : rule.getDeclarations()) {
//    			switch(declaration.getProperty()) {
//    				case "color" -> component.setForeground(Color.decode(declaration.getValue()));
//    				case "font-family" -> component.setFont(new Font(declaration.getValue(), Font.PLAIN, 14));
//    				case "text-align" -> {
//    					if (component instanceof JLabel label) {
//    						if (declaration.getValue().equals("center")) {
//    							label.setHorizontalAlignment(SwingConstants.CENTER);
//    						}
//    					}
//    				}
//    				// Ajoutez d'autres styles...
//    			}
//    		}
//    	}
//    	
//    	return component;
//    }
}