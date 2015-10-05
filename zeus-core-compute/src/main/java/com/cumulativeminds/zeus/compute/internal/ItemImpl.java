package com.cumulativeminds.zeus.compute.internal;

import static com.cumulativeminds.zeus.core.meta.Exceptions.UNSPECIFIED_PAYLOAD;
import static com.cumulativeminds.zeus.core.meta.StdFields.effectiveDate;

import java.time.LocalDateTime;

import com.cumulativeminds.zeus.compute.ModelContractViolationException;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.meta.StdFields;
import com.cumulativeminds.zeus.core.spi.EntityObject;
import com.cumulativeminds.zeus.core.spi.Item;
import com.cumulativeminds.zeus.core.spi.ItemState;
import com.cumulativeminds.zeus.core.spi.SourceKey;

public class ItemImpl implements Item {
    private transient Model model;
    private EntityObject payload;

    public ItemImpl(Model model, EntityObject payload) {
        this.payload = payload;

        setModel(model);
        if (payload == null) {
            throw new ModelContractViolationException(UNSPECIFIED_PAYLOAD);
        }
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public String getItemKey() {
        return payload.getTypedValue(model.getKeyPropertyName(), String.class);
    }

    @Override
    public SourceKey getSourceKey() {
        return null;
    }

    @Override
    public EntityObject getPayload() {
        return payload;
    }

    @Override
    public Integer version() {
        return payload.version();
    }

    @Override
    public String scn() {
        return payload.getScn();
    }

    @Override
    public ItemState getState() {
        return payload.getState();
    }

    @Override
    public LocalDateTime getEffectiveDate() {
        return payload.getTypedValue(effectiveDate.name(), LocalDateTime.class);
    }

    @Override
    public LocalDateTime getLastUpdated() {
        return payload.getTypedValue(StdFields.lastUpdated.name(), LocalDateTime.class);
    }

    @Override
    public void state(ItemState state) {
        payload.getOrCreateMeta().put(StdFields.state.name(), state);
    }

    @Override
    public void lastUpdated(LocalDateTime lastUpdated) {
        payload.put(StdFields.lastUpdated.name(), lastUpdated);
    }

    @Override
    public void scn(String scn) {
        payload.getOrCreateMeta().put(StdFields.scn.name(), scn);
    }

}
