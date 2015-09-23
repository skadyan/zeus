package com.cumulativeminds.zeus.model;

import java.util.HashMap;
import java.util.Map;

public class DataChangeEvent {

    private String modelCode;
    private Map<String, Object> data = new HashMap<>(1);

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void add(String field, Object value) {
        data.put(field, value);
    }
}
