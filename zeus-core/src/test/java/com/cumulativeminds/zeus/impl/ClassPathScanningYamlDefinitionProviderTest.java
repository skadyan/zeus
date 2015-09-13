package com.cumulativeminds.zeus.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ClassPathScanningYamlDefinitionProviderTest {

    @Test
    public void scanYamlResourcesFromClassPath() throws Exception {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        ClassPathScanningYamlDefinitionProvider provider = new ClassPathScanningYamlDefinitionProvider(resourceLoader);

        String basePackage = "com.modeldef.mock";
        Resource[] resources = provider.scan(basePackage);
        assertThat(resources.length, greaterThan(0));
    }

    @Test
    public void tryToLoadSingleYamlFile() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        InputStream yamlSource = new ClassPathResource("/Sample.yaml").getInputStream();
        @SuppressWarnings("unchecked")
        Map<String, Object> definition = mapper.readValue(yamlSource, Map.class);
        System.out.println("def: " + definition);
        assertThat(definition.size(), is(3));
        assertThat(definition.get("tags"), instanceOf(List.class));
        assertThat(((List<?>) definition.get("tags")).size(), is(2));
    }

    @Test
    public void tryToLoadSingpleYamlFileAsPropertyFile() throws Exception {
        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
        factoryBean.setResources(new ClassPathResource("/Sample.yaml"));
        Properties defintion = factoryBean.getObject();
        assertThat(defintion.size(), is(4));
        assertThat(defintion.getProperty("tags[0]"), is("yaml"));
        assertThat(defintion.getProperty("tags[1]"), is("java"));
    }
}
