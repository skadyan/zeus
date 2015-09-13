package com.cumulativeminds.zeus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.auth.SecuritySchemeDefinition;
import com.wordnik.swagger.models.parameters.Parameter;
import com.wordnik.swagger.models.properties.Property;
import com.wordnik.swagger.util.ModelDeserializer;
import com.wordnik.swagger.util.ParameterDeserializer;
import com.wordnik.swagger.util.PropertyDeserializer;
import com.wordnik.swagger.util.SecurityDefinitionDeserializer;

@Configuration
public class ServiceBeans {

    @Bean
    public static Module swaggerModel() {
        SimpleModule module = new SimpleModule("SwaggerModule");
        module.addDeserializer(Property.class, new PropertyDeserializer());
        module.addDeserializer(Model.class, new ModelDeserializer());
        module.addDeserializer(Parameter.class, new ParameterDeserializer());
        module.addDeserializer(SecuritySchemeDefinition.class, new SecurityDefinitionDeserializer());

        return module;
    }

}
