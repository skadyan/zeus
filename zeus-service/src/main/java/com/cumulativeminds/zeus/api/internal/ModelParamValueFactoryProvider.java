package com.cumulativeminds.zeus.api.internal;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelRegistry;

@Singleton
public class ModelParamValueFactoryProvider extends AbstractValueFactoryProvider {
    private static Logger log = LoggerFactory.getLogger(ModelParamValueFactoryProvider.class);
    private ModelRegistry modelRegistry;

    static final class InjectionResolver extends ParamInjectionResolver<ModelParam> {
        public InjectionResolver() {
            super(ModelParamValueFactoryProvider.class);
        }
    }

    private static final class IdentityParamValueFactory extends AbstractContainerRequestValueFactory<Model> {

        private ModelRegistry registry;

        @Context
        private ResourceContext context;

        public IdentityParamValueFactory(ModelRegistry modelRegistry) {
            registry = modelRegistry;
        }

        public Model provide() {
            final UriInfo uriInfo = context.getResource(UriInfo.class);
            // nice but costly call.
            // final String code = uriInfo.getPathSegments().get(0).getPath();
            final String code = extractModelCode(uriInfo);

            return registry.getModelByCode(code);
        }

        private String extractModelCode(final UriInfo uriInfo) {
            String code = null;
            String path = uriInfo.getPath(false);
            int index = path.indexOf('/', 1);
            if (index > 0) {
                code = path.substring(0, index);
            }
            return code;
        }
    }

    @Inject
    public ModelParamValueFactoryProvider(MultivaluedParameterExtractorProvider mpep, ServiceLocator locator,
            ModelRegistry modelRegistry) {
        super(mpep, locator, Parameter.Source.UNKNOWN);
        this.modelRegistry = modelRegistry;
    }

    @Override
    protected Factory<?> createValueFactory(Parameter parameter) {
        Class<?> classType = parameter.getRawType();
        if (classType == null || (!classType.equals(Model.class))) {
            log.warn("ModelParam annotation was not placed on correct object type; Injection might not work correctly!");
            return null;
        }

        return new IdentityParamValueFactory(modelRegistry);
    }

}
