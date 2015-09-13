package com.cumulativeminds.zeus.core.meta;

public enum ModelType {
    ROOT, EMBEDDED, EMBEDDED_INLINE, REF;

    public static ModelType parse(String modelType) {
        modelType = String.valueOf(modelType).toUpperCase();
        for (ModelType type : values()) {
            if (modelType.equals(type.name())) {
                return type;
            }
        }
        return null;
    }
}
