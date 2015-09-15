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
import com.cumulativeminds.zeus.util.JsonObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class ESModelProcessor implements ModelProcessor {
    private TemplateEngine templateEngine;

    private String templateName = "default-elasticsearch-index-def";

    @Value("${search.index.default.number_of_shards:5}")
    private int defaultNumberOfShards;

    @Value("${search.index.default.number_of_replicas:0}")
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
        vars.put("indexdef", createMapOf(model));
        vars.put("helper", new ModelWrapper(model));
        return vars;
    }

    private Map<String, Object> createMapOf(Model model) {
        Map<String, Object> def = new HashMap<>(model.getModelDataIndex().getDefinitionAsMap());
        if (!def.containsKey("number_of_shards")) {
            def.put("number_of_shards", this.defaultNumberOfShards);
        }

        if (!def.containsKey("number_of_replicas")) {
            def.put("number_of_replicas", this.defaultNumberOfReplicas);
        }

        return def;
    }

    public static class ModelWrapper {

        private Model model;
        private JsonObjectMapper jsonObjectMapper;

        public ModelWrapper(Model model) {
            this.model = model;
            this.jsonObjectMapper = new JsonObjectMapper();
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

        public Collection<ModelProperty> findIndexableChildren(ModelProperty p) {
            Collection<ModelProperty> properties = p.getChildProperties();
            Collection<ModelProperty> indexable = new ArrayList<>();
            for (ModelProperty modelProperty : properties) {
                if (modelProperty.isIndexable()) {
                    indexable.add(modelProperty);
                }
            }
            return indexable;
        }

        public String writeJsonValue(Object v) throws JsonProcessingException {
            return jsonObjectMapper.writeValueAsString(v);
        }
    }
}
