package com.cumulativeminds.zeus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;
import org.glassfish.jersey.server.model.Resource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.cumulativeminds.zeus.api.controller.PingController;
import com.cumulativeminds.zeus.api.controller.inbound.MultipartFormDataChangeController;
import com.cumulativeminds.zeus.api.controller.inbound.RawChangeController;
import com.cumulativeminds.zeus.api.controller.inbound.UrlencodedChangeController;
import com.cumulativeminds.zeus.api.internal.ModelInjectionBinder;
import com.cumulativeminds.zeus.core.Zeus;
import com.cumulativeminds.zeus.core.meta.ModelDataSource;
import com.cumulativeminds.zeus.integration.BootstrapModelDefintionLoader;
import com.cumulativeminds.zeus.intergration.ChangeTrigger;
import com.cumulativeminds.zeus.intergration.ModelSourceIntegrationModel;
import com.cumulativeminds.zeus.util.MapFlatterner;
import com.google.common.collect.Sets;

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
        bindModelParamBinder();
        register(JacksonMapperProvider.class);
        register(MultiPartFeature.class);

        register(PingController.class);
        // to make sure we parse all the models
        zeus.getComponent(BootstrapModelDefintionLoader.class);

        ModelService modelService = zeus.getComponent(ModelService.class);

        buildModelAwareAPI(modelService);

        log.info("Service Root Configuration initialized successfully.: {}", settings);
    }

    private void bindModelParamBinder() {
        register(new ModelInjectionBinder());
    }

    private void buildModelAwareAPI(ModelService modelService) {
        Set<Resource> resources = Sets.newIdentityHashSet();
        modelService.getRootModels().forEach(m -> {
            String code = m.getCode();
            String fragment = String.format("/%s", code);
            final Resource.Builder builder = Resource.builder().path(fragment);

            ModelDataSource modelDataSource = m.getModelDataSource();

            ModelSourceIntegrationModel sourceIntegrationModel = modelDataSource.getIntegrationModel();
            if (sourceIntegrationModel == ModelSourceIntegrationModel.PUSH) {
                builder.addChildResource(Resource.builder(UrlencodedChangeController.class).build());
                builder.addChildResource(Resource.builder(RawChangeController.class).build());
                builder.addChildResource(Resource.builder(MultipartFormDataChangeController.class).build());

            }

            ChangeTrigger changeTrigger = modelDataSource.getChangeTrigger();

            if (changeTrigger == null || changeTrigger.isChangeNotificationType()) {

            }

            resources.add(builder.build());

        });

        registerResources(resources);
    }

    private Map<String, Object> settingsAsMap() {
        return MapFlatterner.asFlatternedMap(settings);
    }

    private void enableGZIPEncoder() {
        register(GZipEncoder.class).register(EncodingFilter.class);
    }
}
