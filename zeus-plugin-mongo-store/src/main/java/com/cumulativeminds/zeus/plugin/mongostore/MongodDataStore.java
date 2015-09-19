package com.cumulativeminds.zeus.plugin.mongostore;

import javax.annotation.PostConstruct;

import org.springframework.util.StringUtils;

import com.cumulativeminds.zeus.core.meta.ModelDataStore;
import com.cumulativeminds.zeus.impl.yaml.TypedValueMapAccessor;

public class MongodDataStore extends ModelDataStore {
    private static final String TYPE = "MDB";
    private String collection;

    public MongodDataStore(TypedValueMapAccessor definition) {
        super(TYPE, definition);
    }

    @PostConstruct
    public void parse() {
        String name = getDefinition().getSimpleValue(StoreK.collection);
        if (StringUtils.hasText(name)) {
            setCollection(name);
        }
    }

    public String getCollectionName() {
        return collection;
    }

    void setCollection(String collection) {
        this.collection = collection;
    }
}
