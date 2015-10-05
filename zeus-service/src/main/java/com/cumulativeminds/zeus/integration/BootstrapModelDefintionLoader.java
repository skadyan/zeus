package com.cumulativeminds.zeus.integration;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.CollectionUtils;

import com.cumulativeminds.zeus.core.Zeus;
import com.cumulativeminds.zeus.core.meta.ModelDefinitionParser;
import com.cumulativeminds.zeus.core.meta.ModelRegistry;

@Configuration
@Import(Zeus.class)
public class BootstrapModelDefintionLoader {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BootstrapModelDefintionLoader.class);

    @Inject
    @Named("yamlModelDefintionParser")
    private ModelDefinitionParser yamlDefinitionParser;

    @Inject
    @Named("annotatedModelDefintionParser")
    private ModelDefinitionParser annotatedDefinitionParser;

    @Value("${yamlModelBasePackages}")
    private List<String> yamlModelBasePackages;

    @Value("${annotatedModelBasePackages}")
    private List<String> annotatedModelBasePackages;

    @Inject
    private ModelRegistry modelRegistry;
    
    public BootstrapModelDefintionLoader() {
    }

    @PostConstruct
    public void init() {
        log.info("Model defintion source: Yaml: {}, Annotated: {}", yamlModelBasePackages, annotatedModelBasePackages);
        if (!CollectionUtils.isEmpty(yamlModelBasePackages)) {
            yamlModelBasePackages.forEach(p -> yamlDefinitionParser.scan(p));
        }

        if (!CollectionUtils.isEmpty(annotatedModelBasePackages)) {
            annotatedModelBasePackages.forEach(p -> annotatedDefinitionParser.scan(p));
        }
    }

    public ModelRegistry getModelRegistry() {
        return modelRegistry;
    }
}

