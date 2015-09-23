package com.cumulativeminds.zeus.plugin.flatfile;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.cumulativeminds.zeus.core.meta.ModelDataSource;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

@Configuration
public class FlatfilePlugin {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ModelDataSource ModelDataSource_FlatFile(TypedValueMapAccessor definition) {
        return new FlatfileModelDataSource(definition);
    }
}
