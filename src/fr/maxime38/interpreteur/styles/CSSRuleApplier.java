package fr.maxime38.interpreteur.styles;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import fr.maxime38.interpreteur.parsers.css.utils.CSSRule;

public class CSSRuleApplier {
    private List<CSSRule> cssRules;

    public CSSRuleApplier(List<CSSRule> cssRules) {
        this.cssRules = cssRules;
    }

    public void applyStyles(JComponent component, String tagName) {
        for (CSSRule rule : cssRules) {
            if (rule.getSelector().equals(tagName)) {
                for (var declaration : rule.getDeclarations()) {
                    switch (declaration.getProperty()) {
                        case "color" -> component.setForeground(Color.decode(declaration.getValue()));
                        case "font-family" -> component.setFont(new Font(declaration.getValue(), Font.PLAIN, 14));
                        case "text-align" -> {
                            if (component instanceof JLabel label) {
                                if (declaration.getValue().equals("center")) {
                                    label.setHorizontalAlignment(SwingConstants.CENTER);
                                }
                            }
                        }
                        // Ajoutez d'autres styles...
                    }
                }
            }
        }
    }
}
