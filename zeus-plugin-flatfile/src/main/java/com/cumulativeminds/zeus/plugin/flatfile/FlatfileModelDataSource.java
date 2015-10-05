package com.cumulativeminds.zeus.plugin.flatfile;

import static com.cumulativeminds.zeus.plugin.flatfile.FlatfileK.reader;

import com.cumulativeminds.zeus.core.meta.ModelDataSource;
import com.cumulativeminds.zeus.core.spi.ItemReader;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

public class FlatfileModelDataSource extends ModelDataSource {
    private static final String TYPE = "FlatFile";

    public FlatfileModelDataSource(TypedValueMapAccessor definition) {
        super(TYPE, definition);
    }

    @Override
    public Class<? extends ItemReader> getChangeItemReader() {
        return ChangeItemReaderFlatfileImpl.class;
    }

    public String getReaderType() {
        return getDefinition().getSimpleValue(reader);
    }

}
