package com.cumulativeminds.zeus.core.meta;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cumulativeminds.zeus.core.spi.Version;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;
import com.cumulativeminds.zeus.util.ObjectReference;

public class Model {
    private final String code;
    private final String name;
    private final Map<String, ModelProperty> properties;
    private final Map<String, ModelProperty> propertiesReadOnly;

    private ModelType modelType;
    private String definitionSource;
    private VersionInfo versionInfo;

    private ModelDataSource modelDataSource;
    private ModelDataStore modelDataStore;
    private ModelDataIndex modelDataIndex;

    private Model referencedModel;// only for REF Type model
    private Documentation documentation;
    private ModelProperty keyProperty;
    // derived fields
    private List<ModelProperty> flatternedViewOfProperties;
    private Version currentVersion;
    private List<ModelProperty> sourceIdentifiers;
    private List<ModelProperty> sourceIdentifiersReadOnly;

    private Map<String, TypedValueMapAccessor> features;

    public Model(String code, String name) {
        this.code = code;
        this.name = name;
        this.properties = new HashMap<>();
        this.features = new HashMap<>();
        this.propertiesReadOnly = unmodifiableMap(properties);

        this.sourceIdentifiers = new ArrayList<>();
        this.sourceIdentifiersReadOnly = unmodifiableList(sourceIdentifiers);

    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Map<String, ModelProperty> getProperties() {
        return propertiesReadOnly;
    }

    public List<ModelProperty> getFlatternedViewOfProperties() {
        List<ModelProperty> flatternedProperties = this.flatternedViewOfProperties;
        if (flatternedProperties == null) {
            final List<ModelProperty> f = new ArrayList<>();
            properties.values().forEach(c -> f.addAll(c.getFlatternedViewOfChildProperties()));
            this.flatternedViewOfProperties = flatternedProperties = f;
        }

        return flatternedProperties;
    }

    public String getDefinitionSource() {
        return definitionSource;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public VersionInfo getVersionInfo() {
        return versionInfo;
    }

    public ModelDataSource getModelDataSource() {
        return modelDataSource;
    }

    public ModelDataStore getModelDataStore() {
        return modelDataStore;
    }

    public ModelDataIndex getModelDataIndex() {
        return modelDataIndex;
    }

    public Model getReferencedModel() {
        return referencedModel;
    }

    public Version getCurrentVersion() {
        if (currentVersion == null) {
            final ObjectReference<Version> holder = new ObjectReference<>(versionInfo.getSince());

            getFlatternedViewOfProperties()
                    .forEach(p -> updateMaxVersion(holder, p.getVersionInfo()));

            currentVersion = holder.getReferent();
        }
        return currentVersion;
    }

    private void updateMaxVersion(ObjectReference<Version> holder, VersionInfo info) {
        if (info != null) {
            Version max = holder.getReferent();
            if (max.compareWith(info.getSince()) > 0) {
                max = info.getSince();
            }
            if (max.compareWith(info.getObsolete()) > 0) {
                max = info.getObsolete();
            }

            holder.setReferent(max);
        }
    }

    public Documentation getDocumentation() {
        return documentation;
    }

    void addProperty(ModelProperty property) {
        properties.put(property.getName(), property);
        if (property.hasSource() && property.getSource().isIdentifier()) {
            sourceIdentifiers.add(property);
        }

        resetDerivedFields();
    }

    private void resetDerivedFields() {
        flatternedViewOfProperties = null;
        currentVersion = null;
    }

    void setKeyProperty(ModelProperty keyProperty) {
        this.keyProperty = keyProperty;
    }

    void setDefinitionSource(String definitionSource) {
        this.definitionSource = definitionSource;
    }

    void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    void setVersionInfo(VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }

    void setModelDataSource(ModelDataSource modelDataSource) {
        this.modelDataSource = modelDataSource;
    }

    void setModelDataStore(ModelDataStore modelDataStore) {
        this.modelDataStore = modelDataStore;
    }

    void setModelDataIndex(ModelDataIndex modelDataIndex) {
        this.modelDataIndex = modelDataIndex;
    }

    void setReferencedModel(Model referencedModel) {
        this.referencedModel = referencedModel;
    }

    void setDocumentation(Documentation documentation) {
        this.documentation = documentation;
    }

    void addFeature(String feature, TypedValueMapAccessor data) {
        this.features.put(feature, data);
    }

    public ModelProperty getKeyProperty() {
        return keyProperty;
    }

    public ModelProperty getProperty(String name) {
        return properties.get(name);
    }

    public boolean hasFeature(String feature) {
        return features.containsKey(feature);
    }

    public TypedValueMapAccessor getFeature(String feature) {
        return features.get(feature);
    }

    public TypedValueMapAccessor getFeature(Class<?> feature) {
        return getFeature(feature.getSimpleName());
    }

    public List<ModelProperty> getSourceIdentifiers() {
        return sourceIdentifiersReadOnly;
    }
}
