package com.cumulativeminds.zeus.intergration;

import java.util.List;

public class ChangeTrigger {

    private List<String> fields;
    private String type;

    public ChangeTrigger(String type, List<String> fields) {
        this.type = type;
        this.fields = fields;
    }

    public List<String> getFields() {
        return fields;
    }

    public String getType() {
        return type;
    }
}
