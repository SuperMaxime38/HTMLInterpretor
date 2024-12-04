package fr.maxime38.interpreteur;

import fr.maxime38.interpreteur.css.CSSLexer;
import fr.maxime38.interpreteur.css.CSSToken;
import fr.maxime38.interpreteur.css.CSSTokenType;

public class Main {

	public static void main(String[] args) {
		
//		try (InputStream inputStream = Main.class.getResourceAsStream("/fr/maxime38/interpreteur/test/test.html")) {
//            if (inputStream == null) {
//                throw new IOException("Fichier non trouvé : test.html");
//            }
//
//            // Charger le fichier dans le lexer
//            StreamLexer lexer = new StreamLexer(new InputStreamReader(inputStream));
//            StreamParser parser = new StreamParser(lexer);
//            Node dom = parser.parse();
//
//            System.out.println(dom);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
		
		String css = "body { color: #fff; font-size: 16px; margin: 1.5em; }";
        CSSLexer lexer = new CSSLexer(css);

        CSSToken token;
        while ((token = lexer.nextToken()).getType() != CSSTokenType.EOF) {
            System.out.println(token);
        }
	}

}
