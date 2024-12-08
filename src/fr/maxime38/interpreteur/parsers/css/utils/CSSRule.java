package fr.maxime38.interpreteur.parsers.css.utils;

import java.util.List;

public class CSSRule {
    private String selector;
    private List<CSSDeclaration> declarations;

    public CSSRule(String selector, List<CSSDeclaration> declarations) {
        this.selector = selector;
        this.declarations = declarations;
    }

    public String getSelector() {
        return selector;
    }

    public List<CSSDeclaration> getDeclarations() {
        return declarations;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(selector + " { ");
        for (CSSDeclaration decl : declarations) {
            sb.append(decl.toString()).append(" ");
        }
        sb.append("}");
        return sb.toString();
    }
}