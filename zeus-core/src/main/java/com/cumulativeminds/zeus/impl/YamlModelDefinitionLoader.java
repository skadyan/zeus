package com.cumulativeminds.zeus.impl;

import static com.cumulativeminds.zeus.core.meta.Exceptions.DEFINITION_NOT_FOUND;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.cumulativeminds.zeus.core.Zeus;
import com.cumulativeminds.zeus.core.meta.IllegalModelException;
import com.cumulativeminds.zeus.core.meta.K;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelBuilder;
import com.cumulativeminds.zeus.core.meta.ModelRegistry;
import com.cumulativeminds.zeus.core.spi.VersionProvider;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;
import com.cumulativeminds.zeus.util.YamlObjectMapper;

@Component
public class YamlModelDefinitionLoader extends AbstractModelDefinitionLoader {
    private static final Logger log = LoggerFactory.getLogger(YamlModelDefinitionLoader.class);

    private ResourceLoader resourceLoader;

    private YamlObjectMapper yamlObjectMapper;

    private Map<String, TypedValueMapAccessor> defintions = new TreeMap<>();

    @Inject
    public YamlModelDefinitionLoader(ModelRegistry modelRegistry, VersionProvider versionProvider, ResourceLoader resourceLoader,
            BeanFactory beanFactory) {
        super(modelRegistry, versionProvider, beanFactory);
        this.resourceLoader = resourceLoader;
        this.yamlObjectMapper = new YamlObjectMapper();
    }

    @Override
    public void scan(String basePackage) {
        try {
            ClassPathScanningYamlDefinitionProvider definitionProvider = new ClassPathScanningYamlDefinitionProvider(
                    resourceLoader);
            Resource[] resources = definitionProvider.scan(basePackage);
            prepareResourceMap(basePackage, resources);

            defintions.keySet().forEach(name -> resolve(name));
        } catch (IOException e) {
            Zeus.sneakyThrow(e);
        } finally {
            // we don't need them never
            defintions.clear();
        }
    }

    @Override
    public Model resolve(String source) {
        Model model = super.resolve(source);
        if (model == null) {
            log.info("Loading model: {}", source);
            TypedValueMapAccessor mapAccessor = defintions.get(source);
            if (mapAccessor == null) {
                throw new IllegalModelException(DEFINITION_NOT_FOUND, source);
            }
            model = createModelInstance(source, mapAccessor);
            modelRegistry.register(model);
        }
        return model;
    }

    private void prepareResourceMap(String basePackage, Resource[] resources) throws IOException {
        for (Resource resource : resources) {
            URL url = resource.getURL();
            TypedValueMapAccessor definition = new TypedValueMapAccessor(url, yamlObjectMapper.map(resource));
            String name = definition.getSimpleValue(K.name);
            String code = definition.getSimpleValue(K.code);

            Assert.hasText(name, "name property not found in definition: " + url);
            Assert.hasText(code, "code property not found in definition: " + url);

            defintions.put(name, definition);
            log.info("Model definition found: {}", url);
        }
    }

    private Model createModelInstance(String source, TypedValueMapAccessor definition) {
        return ModelBuilder.from(source, definition)
                .with(modelRegistry, this, versionProvider)
                .withUniqueCodeAndName()
                .withVersion()
                .withModelType()
                .withRootModel().orWithEmbeddedModel()
                .withDefaults()
                .withDocs()
                .withModelDataSource()
                .withModelDataIndex()
                .withProperties()
                .withFeatures()
                .build();
    }

}
