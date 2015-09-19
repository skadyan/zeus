package com.cumulativeminds.zeus.core.meta;

import java.util.Map;

import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

public class ModelDataStore {
    private final String type;
    private TypedValueMapAccessor definition;

    public ModelDataStore(String type, TypedValueMapAccessor definition) {
        this.type = type;
        this.definition = definition;
    }

    public TypedValueMapAccessor getDefinition() {
        return definition;
    }

    public Map<String, Object> getDefinitionAsMap() {
        return definition.asMap();
    }

    public final String getType() {
        return type;
    }

    public void configure(Model model) {
        
    }
}
