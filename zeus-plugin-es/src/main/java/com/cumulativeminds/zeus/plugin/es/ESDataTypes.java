package com.cumulativeminds.zeus.plugin.es;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cumulativeminds.zeus.core.meta.ModelProperty;
import com.cumulativeminds.zeus.core.meta.ModelPropertyType;

public class ESDataTypes {
    public static final String TYPE_STRING = "string";
    public static final String TYPE_DATE = "date";
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_INTEGER = "integer";
    public static final String TYPE_DOUBLE = "double";
    public static final String TYPE_NESTED = "nested";
    public static final String TYPE_OBJECT = "object";
    public static final String NUMBER_OF_SHARDS = "number_of_shards";
    public static final String NUMBER_OF_REPLICAS = "number_of_replicas";

    public static final String SETTING_TYPE = "type";
    public static final String SETTING_INDEX = "index";
    public static final String TYPE_NOT_ANALYZED = "not_analyzed";

    public static String inferType(ModelProperty modelProperty) {
        ModelPropertyType type = modelProperty.getType();
        Class<?> javaType = modelProperty.getJavaType();
        if (type == ModelPropertyType.COLLECTION) {
            return TYPE_NESTED;
        } else if (type == ModelPropertyType.COMPOSITE || type == ModelPropertyType.COMPOSITE_INLINE
                || type == ModelPropertyType.REF) {
            return TYPE_OBJECT;
        } else if (javaType == String.class) {
            return TYPE_STRING;
        } else if (javaType == LocalDate.class) {
            return TYPE_DATE;
        } else if (javaType == LocalDateTime.class) {
            return TYPE_DATE;
        } else if (javaType == Boolean.class) {
            return TYPE_BOOLEAN;
        } else if (Number.class.isAssignableFrom(javaType)) {
            return javaType.getSimpleName().toLowerCase();
        }

        return javaType.getName();
    }

}