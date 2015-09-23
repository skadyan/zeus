package com.cumulativeminds.zeus.meta.schema;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class XModel {
    @JsonProperty(required = true)
    private String code;

    @JsonProperty(required = true)
    private String name;

    @JsonProperty(required = true)
    private String version;

    @JsonProperty(required = true)
    private Map<String, XProperty> properties = new LinkedHashMap<>();

    private XSource source;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, XProperty> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, XProperty> properties) {
        this.properties = properties;
    }

    public XSource getSource() {
        return source;
    }

    public void setSource(XSource source) {
        this.source = source;
    }

    public XModel add(XProperty property) {
        getProperties().put(property.getName(), property);
        return this;
    }

}
