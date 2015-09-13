package com.cumulativeminds.zeus.core;

import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentSettingProvider implements SettingProvider {

    private Environment environment;

    @Inject
    public EnvironmentSettingProvider(Environment environment) {
        this.environment = environment;
    }

    @Override
    public <T> T getSetting(Setting<T> setting) {
        return environment.getProperty(setting.name(), setting.getType());
    }

}
