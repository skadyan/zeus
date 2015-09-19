package com.cumulativeminds.zeus.plugin.mongostore;

import javax.inject.Inject;

import org.junit.Before;

import com.cumulativeminds.zeus.CoreTestCase;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelRegistry;
import com.cumulativeminds.zeus.impl.YamlModelDefinitionLoader;

public abstract class StorePluginTestCase extends CoreTestCase {
    @Inject
    private YamlModelDefinitionLoader yamlModelDefinitionLoader;

    @Inject
    private ModelRegistry modelRegistry;

    protected Model zzModel;

    @Before
    public void setUp() {
        String basePackage = "modeldefs";
        yamlModelDefinitionLoader.scan(basePackage);
        zzModel = modelRegistry.getModelByCode("zz_model");
    }

}