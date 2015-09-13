package com.cumulativeminds.zeus.impl;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import com.cumulativeminds.zeus.CoreTestCase;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelProperty;
import com.cumulativeminds.zeus.core.meta.ModelRegistry;

public class YamlModelDefinitionLoaderTest extends CoreTestCase {
    final String basePackage = "com.modeldef.mock";

    @Inject
    private YamlModelDefinitionLoader yamlModelDefinitionLoader;

    private ModelRegistry registry;

    private Model author;

    @Before
    public void findAndParseDefinitions() {
        yamlModelDefinitionLoader.scan(basePackage);
        registry = yamlModelDefinitionLoader.modelRegistry;
        author = registry.getModelByCode("author");
    }

    @Test
    public void loadAddressModel() throws Exception {
        Model mAddress = registry.getModelByCode("address");
        assertThat(mAddress.getName(), is("mock.Address"));
        assertThat(mAddress.getDocumentation().getExamples(), hasSize(greaterThan(0)));
    }

    @Test
    public void verisionInfoIsLoaded() throws Exception {
        // validate version
        assertThat(author.getVersionInfo().getSince(), is(VersionImpl.V_1_0));
        assertThat(author.getVersionInfo().getObsolete(), is(VersionImpl.UNSPECIFIED));
    }

    @Test
    public void modelPropertiesAreLoaded() throws Exception {
        assertThat(author.getProperties().keySet(), hasSize(is(8)));
    }

    @Test
    public void modelKeyPropertyIsAssigned() throws Exception {
        ModelProperty keyProperty = author.getKeyProperty();

        assertThat(keyProperty.getName(), is("id"));
    }

}
