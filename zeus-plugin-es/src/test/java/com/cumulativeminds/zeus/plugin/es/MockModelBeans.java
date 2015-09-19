package com.cumulativeminds.zeus.plugin.es;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.cumulativeminds.zeus.core.meta.ModelDataSource;
import com.cumulativeminds.zeus.core.meta.ModelDataStore;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

@Configuration
public class MockModelBeans {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ModelDataSource ModelDataSource_SQL(TypedValueMapAccessor definition) {
        return new ModelDataSource("es-mock", definition);
    }
    

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static ModelDataStore ModelDataStore_MDB(TypedValueMapAccessor definition) {
        return new ModelDataStore("MOCK", definition);
    }
    
}
