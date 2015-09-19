package com.cumulativeminds.zeus.impl;

import org.springframework.beans.factory.BeanFactory;

import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelDataIndex;
import com.cumulativeminds.zeus.core.meta.ModelDataSource;
import com.cumulativeminds.zeus.core.meta.ModelDataStore;
import com.cumulativeminds.zeus.core.meta.ModelDefinitionParser;
import com.cumulativeminds.zeus.core.meta.ModelRegistry;
import com.cumulativeminds.zeus.core.spi.VersionProvider;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

public abstract class AbstractModelDefinitionLoader implements ModelDefinitionParser {
    protected final VersionProvider versionProvider;
    protected final ModelRegistry modelRegistry;
    protected final BeanFactory beanFactory;

    public AbstractModelDefinitionLoader(ModelRegistry modelRegistry, VersionProvider versionProvider, BeanFactory beanFactory) {
        this.modelRegistry = modelRegistry;
        this.versionProvider = versionProvider;
        this.beanFactory = beanFactory;
    }

    @Override
    public Model resolve(String source) {
        return modelRegistry.getModelBySource(source);
    }

    @Override
    public ModelDataSource parseModelDataSource(String type, TypedValueMapAccessor definition) {
        return ModelDataSource.class.cast(beanFactory.getBean("ModelDataSource_" + type, definition));
    }

    @Override
    public ModelDataIndex parseModelDataIndex(String type, TypedValueMapAccessor definition) {
        return ModelDataIndex.class.cast(beanFactory.getBean("ModelDataIndex_" + type, definition));
    }

    @Override
    public ModelDataStore parseModelDataStore(String type, TypedValueMapAccessor definition) {
        return ModelDataStore.class.cast(beanFactory.getBean("ModelDataStore_" + type, definition));
    }
}
