package com.cumulativeminds.zeus.core.meta;

import com.cumulativeminds.zeus.core.Keyword;

public enum K implements Keyword {
    name, code, since, obsolete,

    modelType, ref,

    propertyGroup,

    documentation, title, description, example, examples,

    properties, type, key, meta, collection, mapped, indexable,

    source, nameOrExpr, alias, identifier, integrationModel, changeTrigger, data,

    features,

    index,

    store;

    public static String NONE = "(none)";

}
