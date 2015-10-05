package com.cumulativeminds.zeus.core.spi;

import java.util.LinkedHashMap;

import com.cumulativeminds.zeus.core.meta.StdFields;

public class EntityObject extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = -3343053969560744464L;

    public <T> T getTypedValue(String name, Class<T> type) {
        return type.cast(get(name));
    }

    public Integer version() {
        return getTypedValue(StdFields.version.name(), Integer.class);
    }

    public EntityObject getMeta() {
        return getTypedValue(StdFields._meta.name(), EntityObject.class);
    }

    public String getScn() {
        return getMeta().getTypedValue(StdFields.scn.name(), String.class);
    }

    public ItemState getState() {
        return getMeta().getTypedValue(StdFields.state.name(), ItemState.class);
    }

    public EntityObject getOrCreateMeta() {
        EntityObject meta = getMeta();
        if (meta == null) {
            put(StdFields._meta.name(), meta = new EntityObject());
        }

        return meta;
    }

}
