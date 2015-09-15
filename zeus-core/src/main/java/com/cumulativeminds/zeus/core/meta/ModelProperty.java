package com.cumulativeminds.zeus.core.meta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ModelProperty {
    private final String name;
    private final Class<?> javaType;
    private ModelPropertyType type;
    private PropertySource source;
    private PropertyIndex index;
    private String definitionSource;

    private Model owner;
    private Model referencedModel;

    private int order;

    private VersionInfo versionInfo;

    private Documentation documentation;
    private boolean keyProperty;
    private String group;
    private boolean indexable;
    private boolean primitive;

    public ModelProperty(String name, Class<?> javaType) {
        this.name = name;
        this.javaType = javaType;
    }

    public ModelPropertyType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public Model getOwner() {
        return owner;
    }

    public PropertySource getSource() {
        return source;
    }

    public int getOrder() {
        return order;
    }

    public String getDefinitionSource() {
        return definitionSource;
    }

    public VersionInfo getVersionInfo() {
        return versionInfo;
    }

    public Documentation getDocumentation() {
        return documentation;
    }

    public Model getReferencedModel() {
        return referencedModel;
    }

    void setOwner(Model owner) {
        this.owner = owner;
    }

    void setSource(PropertySource source) {
        this.source = source;
    }

    void setOrder(int order) {
        this.order = order;
    }

    void setDefinitionSource(String definitionSource) {
        this.definitionSource = definitionSource;
    }

    void setVersionInfo(VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }

    void setDocumentation(Documentation documentation) {
        this.documentation = documentation;
    }

    void setReferencedModel(Model referencedModel) {
        this.referencedModel = referencedModel;
    }

    void setKeyProperty(boolean keyProperty) {
        this.keyProperty = keyProperty;
    }

    void setType(ModelPropertyType type) {
        this.type = type;
    }

    public List<ModelProperty> getFlatternedViewOfChildProperties() {
        List<ModelProperty> children = new ArrayList<>();
        children.add(this);
        if (isCompositeProperty()) {
            children.addAll(referencedModel.getFlatternedViewOfProperties());
        }

        return children;
    }

    public boolean isCompositeProperty() {
        return !(type == ModelPropertyType.SIMPLE);
    }

    public boolean isKeyProperty() {
        return keyProperty;
    }

    void setGroup(String pg) {
        this.group = pg;
    }

    void setIndexable(boolean indexable) {
        this.indexable = indexable;
    }

    void setPrimitive(boolean primitive) {
        this.primitive = primitive;
    }

    void setIndex(PropertyIndex index) {
        this.index = index;
    }
    public boolean isPrimitive() {
        return primitive;
    }

    public String getGroup() {
        return group;
    }

    public Collection<ModelProperty> getChildProperties() {
        Collection<ModelProperty> childern = Collections.emptyList();
        if (isCompositeProperty()) {
            childern = referencedModel.getProperties().values();
        }
        return childern;
    }

    public boolean hasSource() {
        return source != null && !source.isIgnored();
    }

    public boolean isIndexable() {
        return indexable;
    }

    public PropertyIndex getIndex() {
        return index;
    }
}
