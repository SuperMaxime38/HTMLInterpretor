package fr.maxime38.interpreteur;

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
	}

}
