package com.cumulativeminds.zeus.core;

import javax.inject.Inject;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
@EnableConfigurationProperties
public class Zeus {

    @Inject
    private ApplicationContext applicationContext;

    public Zeus() {
    }

    public <T> T getComponent(Class<T> type) {
        return applicationContext.getBean(type);
    }

    public static <T> T ifNull(T nullableValue, T notNullableValue) {
        return nullableValue == null ? notNullableValue : nullableValue;
    }

    public static void sneakyThrow(Throwable ex) {
        Zeus.<RuntimeException> sneakyThrow0(ex);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> T sneakyThrow0(Throwable ex) throws T {
        throw (T) ex;
    }

    @Bean
    public static ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();

        source.setBasename("exceptions/messages");
        source.setUseCodeAsDefaultMessage(true);
        source.setAlwaysUseMessageFormat(true);

        CoreException.setResourceBundle(source);

        return source;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
