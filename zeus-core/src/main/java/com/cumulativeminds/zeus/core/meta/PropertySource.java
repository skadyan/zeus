package com.cumulativeminds.zeus.core.meta;

public class PropertySource {
    public static final PropertySource IGNORED = new PropertySource(K.NONE, K.NONE, false);
    private String nameOrExpr;
    private String alias;
    private boolean identifier;

    PropertySource(String nameOrExpr, String alias, boolean identifier) {
        this.nameOrExpr = nameOrExpr;
        this.alias = alias;
        this.identifier = identifier;
    }

    public String getAlias() {
        return alias;
    }

    public String getNameOrExpr() {
        return nameOrExpr;
    }

    public boolean isIdentifier() {
        return identifier;
    }

    public boolean isIgnored() {
        return IGNORED == this;
    }

}
