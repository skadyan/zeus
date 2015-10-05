package com.cumulativeminds.zeus;

import javax.servlet.Servlet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;

import com.cumulativeminds.zeus.core.Zeus;
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
    public static Module swaggerJsonModel() {
        SimpleModule module = new SimpleModule("SwaggerModule");
        module.addDeserializer(Property.class, new PropertyDeserializer());
        module.addDeserializer(Model.class, new ModelDeserializer());
        module.addDeserializer(Parameter.class, new ParameterDeserializer());
        module.addDeserializer(SecuritySchemeDefinition.class, new SecurityDefinitionDeserializer());

        return module;
    }

    @Bean
    public static ConversionServiceFactoryBean conversionService() {
        return new ConversionServiceFactoryBean();
    }

    @Bean
    @ConditionalOnProperty(prefix = DataSourceProperties.PREFIX, name = "webConsolePath")
    @ConditionalOnClass(name = "org.h2.server.web.WebServlet")
    public static ServletRegistrationBean h2servletRegistration(
            @Value("${" + DataSourceProperties.PREFIX + ".webConsolePath}") String path) {
        return new ServletRegistrationBean(
                Zeus.instantiate("org.h2.server.web.WebServlet", Servlet.class),
                path + "/*");
    }
}
