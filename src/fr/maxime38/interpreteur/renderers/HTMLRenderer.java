package fr.maxime38.interpreteur.renderers;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.maxime38.interpreteur.parsers.Node;
import fr.maxime38.interpreteur.styles.CSSRuleApplier;

public class HTMLRenderer {
    public static JPanel render(Node dom, CSSRuleApplier styleApplier) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        traverseDOM(dom, panel, styleApplier);
        return panel;
    }

    private static void traverseDOM(Node node, JComponent parent, CSSRuleApplier styleApplier) {
        if (node.getTagName().equals("body")) {
            for (Node child : node.getChildren()) {
                traverseDOM(child, parent, styleApplier);
            }
        } else if (node.getTagName().equals("h1")) {
            JLabel label = new JLabel(node.getTextContent());
            styleApplier.applyStyles(label, "h1");
            parent.add(label);
        } else if (node.getTagName().equals("p")) {
            JLabel label = new JLabel(node.getTextContent());
            styleApplier.applyStyles(label, "p");
            parent.add(label);
        }
        // Ajoutez des cas pour d'autres balises...
    }
}