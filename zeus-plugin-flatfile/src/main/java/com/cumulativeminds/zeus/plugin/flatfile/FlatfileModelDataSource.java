package com.cumulativeminds.zeus.plugin.flatfile;

import com.cumulativeminds.zeus.core.meta.ModelDataSource;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

public class FlatfileModelDataSource extends ModelDataSource {
    private static final String TYPE = "FlatFile";

    public FlatfileModelDataSource(TypedValueMapAccessor definition) {
        super(TYPE, definition);
    }

}
