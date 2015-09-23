package com.cumulativeminds.zeus.meta.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class XProperty {

    @JsonIgnore
    private String name;

    @JsonProperty(required = true)
    private String type;

    private String format;

    public XProperty(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public static XProperty of(String name, String type) {
        return new XProperty(name, type);
    }

    public static XProperty of(String name) {
        return of(name, "string");
    }
}
