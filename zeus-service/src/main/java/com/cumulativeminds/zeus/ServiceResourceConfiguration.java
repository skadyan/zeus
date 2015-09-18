package com.cumulativeminds.zeus;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.cumulativeminds.zeus.api.controller.Controllers;
import com.cumulativeminds.zeus.core.Zeus;
import com.cumulativeminds.zeus.util.MapFlatterner;

@Configuration
@ApplicationPath("/api")
@ConfigurationProperties("zeus.jersey")
public class ServiceResourceConfiguration extends ResourceConfig {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ServiceResourceConfiguration.class);
    /**
     * directly mapped with property defined in context prefixed with:
     * <code>zeus.jersey.settings</code>
     */
    private Map<String, Object> settings = new HashMap<String, Object>();

    @Inject
    Zeus zeus;

    public ServiceResourceConfiguration() {
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    @PostConstruct
    public void initialize() {
        addProperties(settingsAsMap());
        enableGZIPEncoder();

        register(JacksonMapperProvider.class);

        packages(Controllers.class.getPackage().getName());

        log.info("Service Root Configuration initialized successfully.: {}", settings);
    }

    private Map<String, Object> settingsAsMap() {
        return MapFlatterner.asFlatternedMap(settings);
    }

    private void enableGZIPEncoder() {
        register(GZipEncoder.class).register(EncodingFilter.class);
    }
}
