package fr.maxime38.interpreteur.utils;

import java.util.ArrayList;
import java.util.List;

import fr.maxime38.interpreteur.parsers.Node;

public class DOMUtils {
    public static List<String> extractStyles(Node dom) {
        List<String> styles = new ArrayList<>();
        traverseDOM(dom, styles);
        return styles;
    }

    private static void traverseDOM(Node node, List<String> styles) {
        if (node.getTagName().equals("style")) {
            styles.add(node.getTextContent()); // Ajouter le contenu du nœud <style>
        }

        for (Node child : node.getChildren()) {
            traverseDOM(child, styles); // Parcourir récursivement les enfants
        }
    }
}
