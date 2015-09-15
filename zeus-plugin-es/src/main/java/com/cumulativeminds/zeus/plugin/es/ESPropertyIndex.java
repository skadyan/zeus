package com.cumulativeminds.zeus.plugin.es;

import java.util.HashMap;
import java.util.Map;

import com.cumulativeminds.zeus.core.meta.PropertyIndex;

public class ESPropertyIndex extends PropertyIndex {
    public ESPropertyIndex(Map<String, Object> definition) {
        super(new HashMap<>(definition));
    }

    void setType(String type) {
        definition.put(EsK.type.name(), type);
    }

    void setIndex(String index) {
        definition.put(EsK.index.name(), index);
    }
}
