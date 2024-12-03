package fr.maxime38.interpreteur;

public class Main {

	public static void main(String[] args) {
		String html = """
				<html>
					<body>
						<div id="main" class="container">
							<p>Hello, World!</p>
							<img src="C://poutre.png" />
						</div>
						<style>
							body {
                				background-color: black;
							}
						</style>
					</body>
				</html>
	            """;

	        Lexer lexer = new Lexer(html);
	        Parser parser = new Parser(lexer);
	        Node dom = parser.parse();

	        System.out.println(dom);
	}

}
