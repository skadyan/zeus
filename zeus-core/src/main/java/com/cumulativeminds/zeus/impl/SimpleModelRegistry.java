package com.cumulativeminds.zeus.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.core.meta.IllegalModelException;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelRegistry;

@Component
public class SimpleModelRegistry implements ModelRegistry {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SimpleModelRegistry.class);

    private final Map<String, Model> modelsByCode;
    private final Map<String, Model> modelsBySource;

    public SimpleModelRegistry() {
        modelsByCode = new HashMap<>();
        modelsBySource = new HashMap<>();
    }

    @Override
    public Iterator<Model> iterator() {
        return modelsByCode.values().iterator();
    }

    @Override
    public Model getModelByCode(String code) {
        Model model = modelsByCode.get(code);
        if (model == null) {
            throw new IllegalArgumentException("No such model with code '" + code + "'");
        }

        return model;
    }

    @Override
    public Model getModelBySource(String source) {
        return modelsBySource.get(source);
    }

    @Override
    public boolean isRegistered(String code) {
        return modelsByCode.containsKey(code);
    }

    @Override
    public void register(Model model) {
        String source = model.getDefinitionSource();
        String code = model.getCode();
        if (isRegistered(code)) {
            throw new IllegalModelException("DUPLICATE_MODEL_CODE",
                    getModelByCode(code).getDefinitionSource(),
                    model.getDefinitionSource());
        }

        modelsByCode.put(code, model);
        modelsBySource.put(source, model);
        log.info("Model with code '{}' registered from '{}'", code, source);
    }

}
