package com.cumulativeminds.zeus.core.meta;

import java.util.Map;

public class PropertyIndex {
    protected Map<String, Object> definition;

    public PropertyIndex(Map<String, Object> definition) {
        this.definition = definition;
    }

    public Map<String, Object> getDefinition() {
        return definition;
    }

}
