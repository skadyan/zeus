package com.cumulativeminds.zeus.core.meta;

import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

public interface ModelDefinitionParser {
    void scan(String basePackage);

    Model resolve(String source);

    ModelDataSource parseModelDataSource(String type, TypedValueMapAccessor definition);

    ModelDataIndex parseModelDataIndex(String type, TypedValueMapAccessor definition);
    
    ModelDataStore parseModelDataStore(String type, TypedValueMapAccessor definition);
}
