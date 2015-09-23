package com.cumulativeminds.zeus.intergration;

import java.util.Set;

public class ChangeTrigger {
    private Set<String> fields;

    private String type;

    public ChangeTrigger(String type, Set<String> fields) {
        this.type = type;
        this.fields = fields;
    }

    public Set<String> getFields() {
        return fields;
    }

    public String getType() {
        return type;
    }

    public boolean isChangeNotificationType() {
        return "change_notification".equals(type);
    }
}
