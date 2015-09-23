package com.cumulativeminds.zeus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelRegistry;
import com.cumulativeminds.zeus.core.meta.ModelType;

@Component
public class ModelService {
    private ModelRegistry registry;

    private List<Model> rootModels;

    @Inject
    public ModelService(ModelRegistry registry) {
        this.registry = registry;
    }

    public ModelRegistry getRegistry() {
        return registry;
    }

    public List<Model> getRootModels() {
        List<Model> rootModels = this.rootModels;
        if (rootModels == null) {
            ArrayList<Model> list = new ArrayList<Model>();
            registry.forEach(m -> {
                if (m.getModelType() == ModelType.ROOT)
                    list.add(m);
            });
            this.rootModels = rootModels = list;
        }

        return rootModels;

    }
}
