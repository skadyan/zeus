package com.cumulativeminds.zeus.plugin.es;

import static org.springframework.util.Assert.isInstanceOf;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.core.meta.IllegalModelException;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelProperty;
import com.cumulativeminds.zeus.core.meta.ModelType;
import com.cumulativeminds.zeus.impl.ModelProcessor;
import com.cumulativeminds.zeus.template.TemplateEngine;

@Component
public class ESModelProcessor implements ModelProcessor {
    private TemplateEngine templateEngine;

    private String templateName = "default-es-mapper";

    @Value("${search.index.default.number_of_shards:5}")
    private int defaultNumberOfShards;

    @Value("${search.index.default.number_of_shards:0}")
    private int defaultNumberOfReplicas;

    @Inject
    public ESModelProcessor(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public void process(Model model, Object accumulator) {
        if (model.getModelType() != ModelType.ROOT) {
            throw new IllegalModelException("only root model can be processed", model.getName());
        }

        isInstanceOf(Writer.class, accumulator, "Accumulator must be a java.io.Writer");
        Writer writer = (Writer) accumulator;

        Map<String, Object> vars = createMapperModel(model);
        templateEngine.process(writer, templateName, vars);
    }

    private Map<String, Object> createMapperModel(Model model) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("model", model);
        vars.put("index", model.getModelDataIndex());
        vars.put("indexdef", model.getModelDataIndex().getDefinitionAsMap());
        vars.put("helper", new ModelWrapper(model));
        return vars;
    }

    public static class ModelWrapper {

        private Model model;

        public ModelWrapper(Model model) {
            this.model = model;
        }

        public Collection<ModelProperty> getIndexableProperties() {
            Collection<ModelProperty> properties = model.getProperties().values();
            Collection<ModelProperty> indexable = new ArrayList<>();
            for (ModelProperty modelProperty : properties) {
                if (modelProperty.isIndexable()) {
                    indexable.add(modelProperty);
                }
            }
            return indexable;
        }
    }
}
