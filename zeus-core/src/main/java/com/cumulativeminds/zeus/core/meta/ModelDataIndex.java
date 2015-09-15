package com.cumulativeminds.zeus.core.meta;

import java.util.Map;

import org.springframework.util.StringUtils;

import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

public class ModelDataIndex {
    private String type;
    private String indexName;
    private TypedValueMapAccessor definition;

    public ModelDataIndex(String type, TypedValueMapAccessor definition) {
        this.type = type;
        this.definition = definition;
    }

    public TypedValueMapAccessor getDefinition() {
        return definition;
    }

    public Map<String, Object> getDefinitionAsMap() {
        return definition.asMap();
    }

    public String getIndexName() {
        return indexName;
    }

    public String getType() {
        return type;
    }

    void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public void configure(Model model) {
        if (StringUtils.isEmpty(indexName)) {
            setIndexName(model.getCode());
        }
    }

    protected PropertyIndex newPropertyDefinition(ModelProperty modelProperty, TypedValueMapAccessor definition) {
        PropertyIndex index = new PropertyIndex(definition.asMap());

        return index;
    }
}
