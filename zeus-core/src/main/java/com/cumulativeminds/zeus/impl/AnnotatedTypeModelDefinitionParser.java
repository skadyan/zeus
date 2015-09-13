package com.cumulativeminds.zeus.impl;

import javax.inject.Inject;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.core.meta.ModelRegistry;
import com.cumulativeminds.zeus.core.spi.VersionProvider;

@Component
public class AnnotatedTypeModelDefinitionParser extends AbstractModelDefinitionLoader {

    @Inject
    public AnnotatedTypeModelDefinitionParser(ModelRegistry modelRegistry, VersionProvider versionProvider,
            BeanFactory beanFactory) {
        super(modelRegistry, versionProvider, beanFactory);
    }

    @Override
    public void scan(String pkg) {

    }

}
