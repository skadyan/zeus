package com.cumulativeminds.zeus.util;

import org.junit.Test;

import com.cumulativeminds.zeus.meta.schema.XModel;
import com.cumulativeminds.zeus.meta.schema.XProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

public class YamlObjectMapperTest {
    @Test
    public void generateSchema() throws Exception {
        ObjectMapper m = new ObjectMapper();
        SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
        m.acceptJsonFormatVisitor(m.constructType(XModel.class), visitor);
        JsonSchema jsonSchema = visitor.finalSchema();
        System.out.println(m.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema));
    }

    @Test
    public void generateYaml() throws Exception {
        YamlObjectMapper yamlObject = new YamlObjectMapper();
        XModel model = new XModel();
        model.setCode("airport");
        model.setName("Airport");
        model
                .add(XProperty.of("id", "string"))
                .add(XProperty.of("name"));

        String asString = yamlObject.writeValueAsString(model);

        System.out.println("Yaml object is:" + asString);
    }

}
