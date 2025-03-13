package fr.maxime38.interpreteur.utils;

public class MessHandler {
	
	public static boolean isFile(String source) {
		/**
		 * Returns true if is a file, False if is a URL
		 * 
		 * Throws an exception if the source is not a file or a URL
		 * 
		 */
		
		String side = source.substring(0, 4);
		if (!side.equals("file") && !side.equals("http")) {
			throw new IllegalArgumentException("The source must be a file or a URL");
		}
		if(side.contains("http")) {
			return false;
		}
		
		return true;
	}
}
