package fr.maxime38.interpreteur;

public enum TokenType {
	OPENING_TAG,    // Balise ouvrante : <html>, <div>, etc.
    CLOSING_TAG,    // Balise fermante : </html>, </div>, etc.
    CLOSE_TAG,      // Fin d'une balise : ">" après une balise ouvrante
    SELF_CLOSING_TAG,  // Balise auto-fermante, ex. <img />
    TEXT,           // Texte entre balises
    RAW_CONTENT,       // Contenu brut entre balises <style> ou <script>
    EOF             // Fin du fichier
}
