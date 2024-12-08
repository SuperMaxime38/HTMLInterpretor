package fr.maxime38.interpreteur.parsers.css;

import java.util.HashMap;

public class CSSTranslator {
	
	public CSSTranslator(String css, boolean globalSelector) {
		CSSLexer lexer = new CSSLexer(css);
		CSSToken token;

        while ((token = lexer.nextToken()).getType() != CSSTokenType.EOF) {
            System.out.println(token);
        }
        System.out.println("=========================");
        
		lexer = new CSSLexer(css);
		
		CSSParser2 parser = new CSSParser2(lexer, globalSelector);
		HashMap<String, HashMap<String, String>> properties = parser.parse();
		System.out.println("rules:" + properties.toString());
		
	}
}
