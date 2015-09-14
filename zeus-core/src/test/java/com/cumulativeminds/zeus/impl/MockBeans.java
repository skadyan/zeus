package com.cumulativeminds.zeus.impl;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.cumulativeminds.zeus.core.meta.ModelDataIndex;
import com.cumulativeminds.zeus.core.meta.ModelDataSource;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

@Configuration
public class MockBeans {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static ModelDataSource ModelDataSource_MOCK(TypedValueMapAccessor definition) {
        return new ModelDataSource("MOCK", definition);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static ModelDataIndex ModelDataIndex_ES(TypedValueMapAccessor definition) {
        return new ModelDataIndex("MOCK", definition);
    }
}
