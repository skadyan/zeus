package com.cumulativeminds.zeus.core.spi;

import java.util.Map;

public class Change {
    public static final int PRIORITY_HIGHEST = 0;
    public static final int PRIORITY_NORMAL = 5;
    public static final int PRIORITY_LOWEST= 10;
    private String code;
    private Map<String, Object> data;

    public Change(String code, Map<String, Object> data) {
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
