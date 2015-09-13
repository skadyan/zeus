package com.cumulativeminds.zeus.intergration;

import java.io.Serializable;
import java.util.Map;

public class ChangeNotification implements Serializable {
    private static final long serialVersionUID = -6749713023499735677L;

    private String entityCode;

    private Map<String, Object> data;

    public ChangeNotification() {
    }

    public ChangeNotification(String entityCode, Map<String, Object> data) {
        this.entityCode = entityCode;
        this.data = data;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getSimpleValue(String key) {
        return getTypedValue(key, String.class);
    }

    public Integer getIntValue(String key) {
        return getTypedValue(key, Integer.class);
    }

    public <T> T getTypedValue(String key, Class<T> type) {
        return type.cast(data.get(key));
    }

    public String getEntityCode() {
        return entityCode;
    }
}
