package fr.maxime38.interpreteur;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import fr.maxime38.interpreteur.parsers.Node;
import fr.maxime38.interpreteur.parsers.StreamLexer;
import fr.maxime38.interpreteur.parsers.StreamParser;
import fr.maxime38.interpreteur.parsers.TokenType;
import fr.maxime38.interpreteur.parsers.css.CSSLexer;
import fr.maxime38.interpreteur.parsers.css.CSSParser;
import fr.maxime38.interpreteur.parsers.css.CSSToken;
import fr.maxime38.interpreteur.parsers.css.CSSTokenType;
import fr.maxime38.interpreteur.parsers.css.utils.CSSRule;
import fr.maxime38.interpreteur.renderers.HTMLRenderer;
import fr.maxime38.interpreteur.styles.CSSRuleApplier;
import fr.maxime38.interpreteur.utils.DOMUtils;

public class Main {

	public static void main(String[] args) {
		
		try (InputStream inputStream = Main.class.getResourceAsStream("/fr/maxime38/interpreteur/test/test.html")) {
            if (inputStream == null) {
                throw new IOException("Fichier non trouvé : test.html");
            }

            // Charger le fichier dans le lexer
            StreamLexer lexer = new StreamLexer(new InputStreamReader(inputStream));
            while(lexer.nextToken().getType() != TokenType.EOF) {
            	System.out.println(lexer.nextToken().toString());
            }
//            lexer = new StreamLexer(new InputStreamReader(inputStream));
//            StreamParser parser = new StreamParser(lexer);
//            Node dom = parser.parse();
//
//            System.out.println(dom);
//            List<String> styles = DOMUtils.extractStyles(dom);
//            StringBuilder combinedStyles = new StringBuilder();
//            for (String style : styles) {
//            	combinedStyles.append(style).append("\n");
//            }
//		    CSSLexer csslexer = new CSSLexer(combinedStyles.toString());
//
//		    CSSToken token;
//		    while ((token = csslexer.nextToken()).getType() != CSSTokenType.EOF) {
//		        System.out.println(token);
//		    }
//
//        System.out.println("-----------------------");
//        csslexer = new CSSLexer(combinedStyles.toString());
//        CSSParser cssparser = new CSSParser(csslexer);
//
//        List<CSSRule> rules = cssparser.parse();
//
//        // Afficher les règles analysées
//        for (CSSRule rule : rules) {
//            System.out.println(rule);
//        }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		
		
//		SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("HTML Renderer");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(800, 600);
//
//            try (InputStream inputStream = Main.class.getResourceAsStream("/fr/maxime38/interpreteur/test/test.html")) {
//                if (inputStream == null) {
//                    throw new IOException("Fichier non trouvé : test.html");
//                }
//                JPanel renderedPanel = renderHTML(inputStream);
//                frame.add(new JScrollPane(renderedPanel));
//                frame.setVisible(true);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
		}
	
	private static JPanel renderHTML(InputStream html) throws IOException {
	    StreamLexer lexer = new StreamLexer(new InputStreamReader(html));
	    StreamParser htmlParser = new StreamParser(lexer);
	    Node dom = htmlParser.parse();

	    // Debug: Affichez le DOM pour confirmer qu'il est correct
	    System.out.println(dom);

	    List<String> styles = DOMUtils.extractStyles(dom);
	    StringBuilder combinedStyles = new StringBuilder();
	    for (String style : styles) {
	        combinedStyles.append(style).append("\n");
	    }

	    CSSLexer cssLexer = new CSSLexer(combinedStyles.toString());
	    CSSParser cssParser = new CSSParser(cssLexer);
	    var cssRules = cssParser.parse();

	    CSSRuleApplier styleApplier = new CSSRuleApplier(cssRules);

	    JPanel panel = HTMLRenderer.render(dom, styleApplier);
	    panel.setBackground(Color.LIGHT_GRAY); // Fond temporaire pour tester
	    return panel;
	}


}
