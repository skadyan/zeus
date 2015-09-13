package com.cumulativeminds.zeus.core.meta;

public interface Exceptions {
    // source, name
    String DUPLICATE_MODEL_NAME = "DUPLICATE_MODEL_NAME";

    // source, code
    String DUPLICATE_MODEL_CODE = "DUPLICATE_MODEL_CODE";

    // source, since
    String UNSPECIFIED_MODEL_SINCE_VERSION = "UNSPECIFIED_MODEL_SINCE_VERSION";

    // source, since, obsolete
    String INCORRECT_MODEL_VERSION = "INCORRECT_MODEL_VERSION";

    // source, modelType, array of valid values
    String INVALID_MODEL_TYPE_NAME = "INVALID_MODEL_TYPE_NAME";

    // source
    String MISSING_PROPERTIES_ERROR = "MISSING_PROPERTIES_ERROR";

    // source, propertyName
    String DUPLICATE_PROPERTY = "DUPLICATE_PROPERTY";

    // source, propertyName
    String MISSING_PROPERTY_DATA_TYPE = "MISSING_PROPERTY_DATA_TYPE";

    // map
    String SINGLETON_OBJECT = "SINGLETON_OBJECT";

    // source
    String DEFINITION_NOT_FOUND = "DEFINITION_NOT_FOUND";

    // keyword, definition
    String ILLEGAL_USE_OF_KEYWORD = "ILLEGAL_USE_OF_KEYWORD";

    // source, property, type
    String UNKNOWN_DATA_TYPE = "UNKNOWN_DATA_TYPE";

    // source, property, type, modelType
    String ILLEGAL_REFERENCE_OF_MODEL = "ILLEGAL_REFERENCE_OF_MODEL";

    // source, name
    String ILLEGAL_REFERENCE_BY_REF_MODEL = "ILLEGAL_REFERENCE_BY_REF_MODEL";

    // source, name
    String ILLEGAL_PROPERTY_TYPE = "ILLEGAL_PROPERTY_TYPE";
    // source, name, mappedname
    String INVALID_MAPPED_PROPERTY = "INVALID_MAPPED_PROPERTY";

    // soure, property, mappedProperty, proeprtyJavaType, mappedPropertytype
    String INCOMPATIBLE_TYPE_IN_REF_PROPERTY = "INCOMPATIBLE_TYPE_IN_REF_PROPERTY";

    // source, name, oldname
    String MULTIPLE_KEY_PROPERTY = "MULTIPLE_KEY_PROPERTY";

    // source
    String MISSING_KEY_PROPERTY = "MISSING_KEY_PROPERTY";

    // source
    String MISSING_SOURCE_OF_ROOT = "MISSING_SOURCE_OF_ROOT";

    // source, property, nameOrExpr
    String ILLEGAL_USE_OF_SOURCE = "ILLEGAL_USE_OF_SOURCE";
}
