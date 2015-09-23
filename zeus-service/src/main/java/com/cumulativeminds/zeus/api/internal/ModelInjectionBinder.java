package com.cumulativeminds.zeus.api.internal;

import javax.inject.Singleton;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

public final class ModelInjectionBinder extends AbstractBinder {

    /**
     * Implement to provide binding definitions using the exposed binding
     * methods.
     */
    @Override
    protected void configure() {
        bind(ModelParamValueFactoryProvider.class).to(ValueFactoryProvider.class).in(Singleton.class);
        bind(ModelParamValueFactoryProvider.InjectionResolver.class).to(
                new TypeLiteral<InjectionResolver<ModelParam>>() {
                }).in(Singleton.class);
    }
}