package com.cumulativeminds.zeus.plugin.sql;

import static org.springframework.util.Assert.isInstanceOf;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.core.features.SourceHasEntityState;
import com.cumulativeminds.zeus.core.meta.IllegalModelException;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelProperty;
import com.cumulativeminds.zeus.core.meta.ModelPropertyType;
import com.cumulativeminds.zeus.core.meta.ModelType;
import com.cumulativeminds.zeus.core.meta.PropertySource;
import com.cumulativeminds.zeus.core.spi.ChangeItemChunk;
import com.cumulativeminds.zeus.core.spi.EntityObject;
import com.cumulativeminds.zeus.impl.ModelProcessor;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;
import com.cumulativeminds.zeus.intergration.ModelSourceIntegrationModel;
import com.cumulativeminds.zeus.template.TemplateEngine;

@Component
public class SqlMappingGenerator implements ModelProcessor {
    private TemplateEngine templateEngine;

    private String templateName = "default-sql-mapper";

    @Inject
    public SqlMappingGenerator(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public void process(Model model, Object accumulator) {
        if (model.getModelType() != ModelType.ROOT) {
            throw new IllegalModelException("only root model can be processed", model.getName());
        }

        isInstanceOf(Writer.class, accumulator, "Accumulator must be a java.io.Writer");
        Writer writer = (Writer) accumulator;

        Map<String, Object> vars = createSqlMapperModel(model);
        templateEngine.process(writer, templateName, vars);
    }

    private Map<String, Object> createSqlMapperModel(Model model) {
        Map<String, Object> vars = new HashMap<>();
        vars.put("model", model);
        vars.put("source", model.getModelDataSource());
        vars.put("helper", new ModelWrapper(model));
        if (model.hasFeature(SourceHasEntityState.NAME)) {
            vars.put("stateUpdate", createStateUpdateModel(model));
        }

        return vars;
    }

    private Map<String, Object> createStateUpdateModel(Model model) {
        Map<String, Object> data = new HashMap<>();
        TypedValueMapAccessor definition = model.getFeature(SourceHasEntityState.class);
        data.put("parameterType", ChangeItemChunk.class.getName());

        List<Map<String, Object>> list = model.getSourceIdentifiers().stream()
                .map(s -> newMapWithTwoEntries("column", "value", s.getSource().getNameOrExpr(), "payload." + s.getName()))
                .collect(Collectors.toList());

        data.put("updateStatusFilter", list);

        data.putAll(definition.asMap());

        return data;
    }

    private Map<String, Object> newMapWithTwoEntries(String key1, String key2, Object value1, Object value2) {
        Map<String, Object> map = new HashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

    public static class ModelWrapper {
        private Model model;

        public ModelWrapper(Model model) {
            this.model = model;
        }

        public boolean hasChangeView() {
            SqlModelDataSource dataSource = (SqlModelDataSource) model.getModelDataSource();
            return dataSource.getChangeViewName() != null;
        }

        public String getTypeName() {
            return EntityObject.class.getName();
        }

        public List<ModelProperty> getPendingChangeNotificationDataProperties() {
            List<String> fields = model.getModelDataSource().getChangeTrigger().getFields();
            List<ModelProperty> list = new ArrayList<>();
            for (String field : fields) {
                ModelProperty property = model.getProperty(field);
                if (property == null) {
                    throw new IllegalModelException("Property " + field
                            + " is specified as change trigger field but it is not defined in model:" + model.getName()
                            + " . Please fix the error in model definition: " + model.getDefinitionSource());
                }
                if (!property.hasSource()) {
                    throw new IllegalModelException("Property " + field
                            + " is specified as change trigger field but its source is not defined in model:" + model.getName()
                            + " . Please fix the error in model definition: " + model.getDefinitionSource());
                }
                list.add(property);
            }
            return list;
        }

        public String stripAliasPrefix(String id) {
            return id;
        }

        public boolean hasFeature(String feature) {
            return model.hasFeature(feature);
        }

        public String getPendingChangeProvider() {
            return model.getModelDataSource().getDefinition().getSimpleValue(SqlK.pendingChangeProvider);
        }

        public boolean isIntegrationModelIsPull() {
            return model.getModelDataSource().getIntegrationModel() == ModelSourceIntegrationModel.PULL;
        }

        public Map<String, Object> getFeatureData(String feature) {
            return hasFeature(feature) ? model.getFeature(feature).asMap() : Collections.emptyMap();
        }

        public String qualify(PropertySource source, String alias) {
            return String.format("%s.%s", alias, source.getNameOrExpr());
        }

        public List<PropertySource> getPropertySources() {
            List<PropertySource> sources = new ArrayList<>();
            List<ModelProperty> flatternProperties = model.getFlatternedViewOfProperties();
            for (ModelProperty modelProperty : flatternProperties) {
                PropertySource source = modelProperty.getSource();
                if (source != null && !source.isIgnored()) {
                    sources.add(source);
                }
            }
            return sources;
        }

        public List<PropertySource> getIdentifierSources() {
            return model.getSourceIdentifiers().stream().map(p -> p.getSource()).collect(Collectors.toList());
        }

        public Set<ModelProperty> getPropertiesInSortedOrderOfMyBatis() {
            Set<ModelProperty> sorted = new TreeSet<>(myBatisComparator());
            List<ModelProperty> list = model.getProperties().values()
                    .stream().filter(e -> e.getSource() != null && !e.getSource().isIgnored())
                    .collect(Collectors.toList());
            sorted.addAll(list);
            return sorted;
        }

        public Set<ModelProperty> nestedPropertiesInSortedOrderOfMyBatis(ModelProperty modelProperty) {
            Set<ModelProperty> sorted = new TreeSet<>(myBatisComparator());
            sorted.addAll(modelProperty.getChildProperties());
            return sorted;
        }

        private Comparator<ModelProperty> myBatisComparator() {
            return new Comparator<ModelProperty>() {
                private EnumMap<ModelPropertyType, Integer> order = new EnumMap<>(ModelPropertyType.class);

                {
                    order.put(ModelPropertyType.SIMPLE, 10);
                    order.put(ModelPropertyType.COMPOSITE, 20);
                    order.put(ModelPropertyType.COMPOSITE_INLINE, 21);
                    order.put(ModelPropertyType.COLLECTION, 30);
                }

                @Override
                public int compare(ModelProperty o1, ModelProperty o2) {
                    int cmp = Boolean.compare(o2.isKeyProperty(), o1.isKeyProperty());
                    if (cmp == 0) {
                        int order1 = order.get(o1.getType());
                        int order2 = order.get(o2.getType());

                        cmp = order1 - order2;
                    }
                    if (cmp == 0) {
                        cmp = o1.getName().compareTo(o2.getName());
                    }
                    return cmp;
                }
            };
        }

        public EntityFilter getFilter(boolean addKeyProperty) {
            List<ModelProperty> properties = getPendingChangeNotificationDataProperties()
                    .stream()
                    .filter(p -> p.getSource().isIdentifier())
                    .collect(Collectors.toList());
            properties.add(model.getKeyProperty());
            return new EntityFilter(properties);
        }

        public EntityFilter getFilter() {
            return getFilter(false);
        }

        public EntityFilter getSpecificFilter() {
            return getFilter(true);
        }

    }

    public static class EntityFilter {

        private List<ModelProperty> filter;

        public EntityFilter(List<ModelProperty> filter) {
            this.filter = filter;
        }

        public List<ModelProperty> getList() {
            return filter;
        }

        public boolean notEmpty() {
            return filter != null && filter.size() > 0;
        }
    }
}
