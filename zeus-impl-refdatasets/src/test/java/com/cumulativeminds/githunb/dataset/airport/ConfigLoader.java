package com.cumulativeminds.githunb.dataset.airport;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cumulativeminds.zeus.core.Zeus;
import com.cumulativeminds.zeus.impl.YamlModelDefinitionLoader;

@Configuration
@Import(Zeus.class)
public class ConfigLoader {

    @Inject
    private YamlModelDefinitionLoader loader;

    public ConfigLoader() {
    }

    @PostConstruct
    public void startup() {
        loader.scan("com.github.datasets");
    }

}
