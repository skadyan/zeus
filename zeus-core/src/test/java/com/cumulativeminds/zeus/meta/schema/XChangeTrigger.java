package com.cumulativeminds.zeus.meta.schema;

public class XChangeTrigger {
    private Type kind;

    public static enum Type {
        change_notification,

        refresh_instance,

        scheduled
    }

    public void setKind(Type kind) {
        this.kind = kind;
    }

    public Type getKind() {
        return kind;
    }
}
