package com.cumulativeminds.zeus.plugin.sql;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.cumulativeminds.zeus.core.meta.ModelDataIndex;
import com.cumulativeminds.zeus.core.meta.ModelDataStore;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

@Configuration
public class MockModelBeans {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static ModelDataIndex ModelDataIndex_ES(TypedValueMapAccessor definition) {
        return new ModelDataIndex("sql-mock", definition);
    }
    

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public static ModelDataStore ModelDataStore_MDB(TypedValueMapAccessor definition) {
        return new ModelDataStore("mdb-mock", definition);
    }
}
