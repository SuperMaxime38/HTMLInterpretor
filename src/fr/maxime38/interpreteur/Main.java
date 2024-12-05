package fr.maxime38.interpreteur;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import fr.maxime38.interpreteur.parsers.Node;
import fr.maxime38.interpreteur.parsers.StreamLexer;
import fr.maxime38.interpreteur.parsers.StreamParser;
import fr.maxime38.interpreteur.parsers.css.CSSLexer;
import fr.maxime38.interpreteur.parsers.css.CSSParser;
import fr.maxime38.interpreteur.renderers.HTMLRenderer;
import fr.maxime38.interpreteur.styles.CSSRuleApplier;

public class Main {

	public static void main(String[] args) {
		
		try (InputStream inputStream = Main.class.getResourceAsStream("/fr/maxime38/interpreteur/test/test.html")) {
            if (inputStream == null) {
                throw new IOException("Fichier non trouvé : test.html");
            }

            // Charger le fichier dans le lexer
            StreamLexer lexer = new StreamLexer(new InputStreamReader(inputStream));
            StreamParser parser = new StreamParser(lexer);
            Node dom = parser.parse();

            System.out.println(dom);
        } catch (IOException e) {
            e.printStackTrace();
        }
		
//			String css = """
//			    body { color: #fff; font-size: 16px; }
//			    .h1 { text-shadow: 2px 2px red; }
//			    div { margin: 10px 20px 30px 40px; }
//			    """;
//		    CSSLexer lexer = new CSSLexer(css);
//
//		    CSSToken token;
//		    while ((token = lexer.nextToken()).getType() != CSSTokenType.EOF) {
//		        System.out.println(token);
//		    }
//
//        System.out.println("-----------------------");
//        lexer = new CSSLexer(css);
//        CSSParser parser = new CSSParser(lexer);
//
//        List<CSSRule> rules = parser.parse();
//
//        // Afficher les règles analysées
//        for (CSSRule rule : rules) {
//            System.out.println(rule);
//        }
		
		SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("HTML Renderer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            try (InputStream inputStream = Main.class.getResourceAsStream("/fr/maxime38/interpreteur/test/test.html")) {
                if (inputStream == null) {
                    throw new IOException("Fichier non trouvé : test.html");
                }
                JPanel renderedPanel = renderHTML(inputStream);
                frame.add(new JScrollPane(renderedPanel));
                frame.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
	}
	
	private static JPanel renderHTML(InputStream html) {
		StreamLexer lexer = new StreamLexer(new InputStreamReader(html));
        StreamParser htmlParser = new StreamParser(lexer);
        Node dom = htmlParser.parse();

        //CSSLexer cssLexer = new CSSLexer();
        CSSParser cssParser = new CSSParser(new CSSLexer(html));
        var cssRules = cssParser.parse();

        CSSRuleApplier styleApplier = new CSSRuleApplier(cssRules);

        return HTMLRenderer.render(dom, styleApplier);
    }
}
