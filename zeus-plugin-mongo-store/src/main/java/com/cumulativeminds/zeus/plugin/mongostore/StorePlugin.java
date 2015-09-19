package com.cumulativeminds.zeus.plugin.mongostore;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.cumulativeminds.zeus.core.meta.ModelDataStore;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

@Configuration
public class StorePlugin {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ModelDataStore ModelDataStore_MDB(TypedValueMapAccessor definition) {
        return new MongodDataStore(definition);
    }

}
