package com.cumulativeminds.zeus.core.meta;

import static com.cumulativeminds.zeus.core.meta.Exceptions.DUPLICATE_MODEL_CODE;
import static com.cumulativeminds.zeus.core.meta.Exceptions.DUPLICATE_MODEL_NAME;
import static com.cumulativeminds.zeus.core.meta.Exceptions.DUPLICATE_PROPERTY;
import static com.cumulativeminds.zeus.core.meta.Exceptions.ILLEGAL_USE_OF_KEYWORD;
import static com.cumulativeminds.zeus.core.meta.Exceptions.INCORRECT_MODEL_VERSION;
import static com.cumulativeminds.zeus.core.meta.Exceptions.INVALID_MODEL_TYPE_NAME;
import static com.cumulativeminds.zeus.core.meta.Exceptions.MISSING_PROPERTIES_ERROR;
import static com.cumulativeminds.zeus.core.meta.Exceptions.MISSING_PROPERTY_DATA_TYPE;
import static com.cumulativeminds.zeus.core.meta.Exceptions.UNSPECIFIED_MODEL_SINCE_VERSION;
import static org.springframework.util.StringUtils.hasText;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cumulativeminds.zeus.core.Shared;
import com.cumulativeminds.zeus.core.Zeus;
import com.cumulativeminds.zeus.core.spi.VersionProvider;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

public class ModelBuilder {
    public static String DEFAULT_INDEX_TYPE = "ES";
    private String source;
    private ModelRegistry modelRegistry;
    private VersionProvider versionProvider;
    private TypedValueMapAccessor definition;
    private Model model;
    private String defaultPropertyGroup;
    private Map<String, Class<?>> types;
    private ModelDefinitionParser modelDefinitionLoader;

    private ModelBuilder(String definitionSource, TypedValueMapAccessor definition) {
        this.source = definitionSource;
        this.definition = definition;
        this.types = buildTypeNameMapping();
    }

    private Map<String, Class<?>> buildTypeNameMapping() {
        Map<String, Class<?>> types = new HashMap<>();
        types.put("string", String.class);
        types.put("int", Integer.class);
        types.put("long", Long.class);
        types.put("double", Double.class);
        types.put("bool", Boolean.class);
        types.put("boolean", Boolean.class);
        types.put("datetime", LocalDateTime.class);
        types.put("date", LocalDate.class);
        types.put("file", File.class);

        return types;
    }

    public Model build() {
        model.setDefinitionSource(source);
        return model;
    }

    public ModelBuilder with(ModelRegistry modelRegistry, ModelDefinitionParser modelDefinitionLoader,
            VersionProvider versionProvider) {
        this.modelRegistry = modelRegistry;
        this.modelDefinitionLoader = modelDefinitionLoader;
        this.versionProvider = versionProvider;
        return this;
    }

    public ModelBuilder withUniqueCodeAndName() {
        String name = definition.getSimpleValue(K.name);
        String code = definition.getSimpleValue(K.code);

        assertModelNameUniqueness(name);
        assertModelCodeUniqueness(code);

        model = new Model(code, name);
        return this;
    }

    private void assertModelCodeUniqueness(String code) {
        Assert.hasText(code, "code is blank");
        if (modelRegistry.isRegistered(code)) {
            throw new IllegalModelException(DUPLICATE_MODEL_CODE, source, code);
        }
    }

    private void assertModelNameUniqueness(String name) {
        Assert.hasText(name, "name is blank");
        modelRegistry.forEach(m -> {
            if (m.getName().equals(name)) {
                throw new IllegalModelException(DUPLICATE_MODEL_NAME, source, name);
            }
        });
    }

    public ModelBuilder withVersion() {
        String since = definition.getSimpleValue(K.since);
        String obsolete = definition.getSimpleValue(K.obsolete);
        VersionInfo versionInfo = new VersionInfo(
                versionProvider.fromText(since),
                versionProvider.fromText(obsolete));

        if (versionProvider.isUnspecified(versionInfo.getSince())) {
            throw new IllegalModelException(UNSPECIFIED_MODEL_SINCE_VERSION, source, since);
        }

        if (isObsoleteVersionIsLowerThanSinceVersion(versionInfo)) {
            throw new IllegalModelException(INCORRECT_MODEL_VERSION, source, since, obsolete);
        }

        model.setVersionInfo(versionInfo);
        return this;
    }

    private boolean isObsoleteVersionIsLowerThanSinceVersion(VersionInfo versionInfo) {
        boolean isObsoleVersionSpecified = !versionInfo.getObsolete().isMajorUnspecified();
        return isObsoleVersionSpecified && versionInfo.getObsolete().compareWith(versionInfo.getSince()) < 0;
    }

    public ModelBuilder withModelType() {
        String modelType = definition.getSimpleValue(K.modelType);
        ModelType type = null;

        if (hasText(modelType)) {
            type = ModelType.parse(modelType);
        } else {
            type = hasText(definition.getSimpleValue(K.ref)) ? ModelType.REF : ModelType.ROOT;
        }

        if (type == null) {
            throw new IllegalModelException(INVALID_MODEL_TYPE_NAME, source, modelType, ModelType.values());
        }

        model.setModelType(type);

        return this;
    }

    public ModelBuilder withDefaults() {
        String pg = definition.getSimpleValue(K.propertyGroup);
        if (StringUtils.hasText(pg)) {
            defaultPropertyGroup = pg;
        }

        return this;
    }

    public ModelBuilder withRootModel() {
        if (model.getModelType() == ModelType.ROOT) {
        }
        return this;
    }

    public ModelBuilder orWithEmbeddedModel() {
        ModelType modelType = model.getModelType();
        if (modelType == ModelType.REF) {
            String refModelName = definition.getSimpleValue(K.ref);
            Model refModel = this.modelDefinitionLoader.resolve(refModelName);
            if (refModel == null || refModel.getModelType() != ModelType.ROOT) {
                throw new IllegalModelException(Exceptions.ILLEGAL_REFERENCE_BY_REF_MODEL, source, refModelName);
            }
            model.setReferencedModel(refModel);
        }

        return this;
    }

    public ModelBuilder withProperties() {
        List<TypedValueMapAccessor> properties = definition.getList(K.properties);
        if (CollectionUtils.isEmpty(properties)) {
            throw new IllegalModelException(MISSING_PROPERTIES_ERROR, source);
        }
        Map<String, ModelProperty> modelProperties = model.getProperties();
        properties.forEach(def -> {
            String propertyName = def.ensureAndGetSingleKey();
            ModelProperty property = newProperty(propertyName, def.getNestedObject(propertyName));

            if (modelProperties.containsKey(propertyName)) {
                throw new IllegalModelException(DUPLICATE_PROPERTY, source, propertyName);
            }
            if (property.isKeyProperty()) {
                ModelProperty kp = model.getKeyProperty();
                if (kp == null) {
                    model.setKeyProperty(property);
                } else {
                    throw new IllegalModelException(Exceptions.MULTIPLE_KEY_PROPERTY, source, propertyName, kp.getName());
                }
            }
            model.addProperty(property);
        });

        if (model.getKeyProperty() == null) {
            if (model.getModelType() != ModelType.EMBEDDED) {
                throw new IllegalModelException(Exceptions.MISSING_KEY_PROPERTY, source);
            }
        }

        return this;
    }

    private ModelProperty newProperty(String name, TypedValueMapAccessor def) {
        String type = def.getSimpleValue(K.type);
        if (!StringUtils.hasText(type)) {
            throw new IllegalModelException(MISSING_PROPERTY_DATA_TYPE, source, name);
        }
        Class<?> primitiveType = toJavaType(type);
        boolean indexable = def.getBooleanValue(K.indexable) || true;
        boolean isarray = def.getBooleanValue(K.collection);

        ModelProperty modelProperty = new ModelProperty(name, primitiveType);

        if (primitiveType == null) {
            modelProperty.setJavaType(isarray ? Collection.class : Object.class);
        }

        if (isarray) {
            modelProperty.setType(ModelPropertyType.COLLECTION);
            modelProperty.setPrimitive(primitiveType != null);
        } else if (primitiveType != null) {
            modelProperty.setType(ModelPropertyType.SIMPLE);
        } else {
            Model referencedModel = this.modelDefinitionLoader.resolve(type);
            if (referencedModel == null) {
                throw new IllegalModelException(Exceptions.UNKNOWN_DATA_TYPE, source, name, type);
            }

            ensureEmbeddedOrRefModelIsReferenced(name, type, referencedModel);
            modelProperty.setType(ModelPropertyType.COMPOSITE);
        }

        if (modelProperty.getType() != ModelPropertyType.SIMPLE) {
            illegalUseIfSpecified(def, K.key, source, name);
        } else {
            modelProperty.setKeyProperty(def.getBooleanValue(K.key));
        }

        if (model.getModelType() == ModelType.REF) {
            if (modelProperty.isCompositeProperty()) {
                throw new IllegalModelException(Exceptions.ILLEGAL_PROPERTY_TYPE, source, name);
            }

            String mapped = def.getSimpleValue(K.mapped);
            if (!hasText(mapped)) {
                mapped = name;
            }
            ModelProperty mappedProperty = model.getReferencedModel().getProperty(mapped);
            if (mappedProperty == null) {
                throw new IllegalModelException(Exceptions.INVALID_MAPPED_PROPERTY, source, name, mapped);
            }
            boolean compatiblle = primitiveType.isAssignableFrom(mappedProperty.getJavaType());
            if (!compatiblle) {
                throw new IllegalModelException(Exceptions.INCOMPATIBLE_TYPE_IN_REF_PROPERTY, source, name, mappedProperty,
                        primitiveType, mappedProperty.getJavaType());
            }

            modelProperty.setIndexable(false);
            if (mappedProperty.isKeyProperty()) {
                modelProperty.setKeyProperty(true);
            }
        } else {
            illegalUseIfSpecified(def, K.mapped, source, name);
        }

        modelProperty.setIndexable(indexable);

        setPropertyGroup(def, modelProperty);
        setPropertySource(def, modelProperty);
        if (model.getModelDataIndex() != null) {
            setPropertyIndex(def, modelProperty);
        }

        return modelProperty;
    }

    private void setPropertyIndex(TypedValueMapAccessor def, ModelProperty modelProperty) {
        if (!modelProperty.isIndexable()) {
            return;
        }

        TypedValueMapAccessor definition = def.getNestedObject(K.index);
        if (definition == null) {
            definition = new TypedValueMapAccessor(Collections.emptyMap());
        }
        PropertyIndex propertyIndex = model.getModelDataIndex().newPropertyDefinition(modelProperty, definition);
        modelProperty.setIndex(propertyIndex);
    }

    private void setPropertySource(TypedValueMapAccessor def, ModelProperty modelProperty) {
        TypedValueMapAccessor definition = def.getNestedObject(K.source, K.nameOrExpr.name());

        String nameOrExpr = null;
        String alias = null;
        boolean identifier = false;

        if (definition != null) {
            nameOrExpr = definition.getSimpleValue(K.nameOrExpr);
            alias = definition.getSimpleValue(K.alias);

            if (definition.containsKey(K.identifier)) {
                identifier = definition.getBooleanValue(K.identifier);
            } else {
                identifier = modelProperty.isKeyProperty();
            }
        } else if (modelProperty.isCompositeProperty()) {
            if (!modelProperty.isPrimitive()) {
                nameOrExpr = Shared.NONE;
            }
        }

        nameOrExpr = Zeus.ifNull(nameOrExpr, modelProperty.getName());
        alias = Zeus.ifNull(alias, nameOrExpr);

        if (K.NONE.equals(nameOrExpr)) {
            modelProperty.setSource(PropertySource.IGNORED);
        } else {
            if (modelProperty.isCompositeProperty() && !modelProperty.isPrimitive()) {
                throw new IllegalModelException(Exceptions.ILLEGAL_USE_OF_SOURCE, source, modelProperty.getName(), nameOrExpr);
            }
            modelProperty.setSource(new PropertySource(nameOrExpr, alias, identifier));
        }

    }

    private void setPropertyGroup(TypedValueMapAccessor def, ModelProperty modelProperty) {
        String pg = def.getSimpleValue(K.propertyGroup);
        if (!hasText(pg)) {
            pg = defaultPropertyGroup;
        }
        modelProperty.setGroup(pg);
    }

    private void ensureEmbeddedOrRefModelIsReferenced(String name, String type, Model referencedModel) {
        if (!(referencedModel.getModelType() == ModelType.REF
                || referencedModel.getModelType() == ModelType.EMBEDDED)) {
            throw new IllegalModelException(Exceptions.ILLEGAL_REFERENCE_OF_MODEL, source, name, type,
                    referencedModel.getModelType());
        }
    }

    private void illegalUseIfSpecified(TypedValueMapAccessor def, K key, Object... objects) {
        if (def.containsKey(key)) {
            throw new IllegalModelException(ILLEGAL_USE_OF_KEYWORD + "_" + key, objects);
        }
    }

    public Class<?> toJavaType(String type, String format) {
        return types.get(type);
    }

    public Class<?> toJavaType(String type) {
        return types.get(type);
    }

    public ModelBuilder withDocs() {
        TypedValueMapAccessor accessor = definition.getNestedObject(K.documentation);
        if (accessor != null) {
            Documentation documentation = new Documentation(
                    accessor.getSimpleValue(K.title),
                    accessor.getSimpleValue(K.description));
            // we process both example and examples
            Object examples = accessor.get(K.examples, Object.class);
            Object example = accessor.get(K.example, Object.class);
            List<String> listOfExamples = new ArrayList<>();

            addToExampleList(examples, listOfExamples);
            addToExampleList(example, listOfExamples);

            listOfExamples.forEach(e -> documentation.addExample(e));
            model.setDocumentation(documentation);
        }

        return this;
    }

    private void addToExampleList(Object examples, List<String> listOfExamples) {
        if (examples != null) {
            if (examples instanceof List) {
                ((List<?>) examples).forEach(e -> listOfExamples.add(String.valueOf(e)));
            } else {
                listOfExamples.add(String.valueOf(examples));
            }
        }
    }

    public static ModelBuilder from(String definitionSource, TypedValueMapAccessor definition) {
        return new ModelBuilder(definitionSource, definition);
    }

    public ModelBuilder withModelDataSource() {
        TypedValueMapAccessor accessor = definition.getNestedObject(K.source);
        if (model.getModelType() != ModelType.ROOT) {
            illegalUseIfSpecified(definition, K.source, source);
        } else {
            if (accessor == null) {
                throw new IllegalModelException(Exceptions.MISSING_SOURCE_OF_ROOT, source);
            }
            String type = accessor.getSimpleValue(K.type);
            ModelDataSource modelDataSource = modelDefinitionLoader.parseModelDataSource(type, accessor);
            modelDataSource.configure(model, this);
            model.setModelDataSource(modelDataSource);
        }

        return this;
    }

    public ModelBuilder withFeatures() {
        List<TypedValueMapAccessor> features = definition.getList(K.features);
        if (!CollectionUtils.isEmpty(features)) {
            features.forEach(def -> {
                String feature = def.ensureAndGetSingleKey();
                TypedValueMapAccessor data = def.getNestedObject(feature);
                model.addFeature(feature, data);
            });
        }
        return this;
    }

    public ModelBuilder withModelDataIndex() {
        TypedValueMapAccessor accessor = definition.getNestedObject(K.index);
        if (model.getModelType() != ModelType.ROOT) {
            illegalUseIfSpecified(definition, K.index, source);
        } else {
            if (accessor == null) {
                Map<String, Object> defaultIndexDefintion = new HashMap<>();
                defaultIndexDefintion.put(K.type.name(), "ES");
                accessor = new TypedValueMapAccessor(defaultIndexDefintion);
            }
            String type = accessor.getSimpleValue(K.type);

            if (StringUtils.isEmpty(type) || Shared.NONE.equals(type)) {
                throw new IllegalModelException(Exceptions.MISSING_INDEX_OF_ROOT, source);
            }

            ModelDataIndex modelDataIndex = modelDefinitionLoader.parseModelDataIndex(type, accessor);
            modelDataIndex.configure(model);
            model.setModelDataIndex(modelDataIndex);
        }

        return this;
    }

    public ModelBuilder withModelDataStore() {
        TypedValueMapAccessor accessor = definition.getNestedObject(K.store);
        if (model.getModelType() != ModelType.ROOT) {
            illegalUseIfSpecified(definition, K.store, source);
        } else {
            if (accessor == null) {
                // no store requirement of this
                return this;
            }
            String type = accessor.getSimpleValue(K.type);
            ModelDataStore modelDataStore = modelDefinitionLoader.parseModelDataStore(type, accessor);
            modelDataStore.configure(model);
            model.setModelDataStore(modelDataStore);
        }

        return this;

    }

}
