package com.cumulativeminds.zeus;

import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

public class YamlUtil {

    public static Properties loadYaml(String resource) {
        YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
        bean.setResources(new ClassPathResource(resource));
        bean.afterPropertiesSet();

        Properties properties = bean.getObject();
        properties.entrySet().forEach(e -> e.setValue(String.valueOf(e.getValue())));
        return properties;
    }
}
