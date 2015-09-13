package com.cumulativeminds.zeus.plugin.es;

import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.ModelDataIndex;
import com.cumulativeminds.zeus.core.spi.Version;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

public class ESModelDataIndex extends ModelDataIndex {
    public static final String TYPE = "ES";
    private String physicalIndexName;

    public ESModelDataIndex(TypedValueMapAccessor definition) {
        super(TYPE, definition);
    }

    public String getPhysicalIndexName() {
        return physicalIndexName;
    }

    void setPhysicalIndexName(String physicalIndexName) {
        this.physicalIndexName = physicalIndexName;
    }

    @Override
    public void configure(Model model) {
        super.configure(model);
        String code = model.getCode();
        Version version = model.getCurrentVersion();

        setPhysicalIndexName(String.format("%s_%s", code, version));

    }
}
