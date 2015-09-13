package com.cumulativeminds.zeus.core.spi;

public interface ChangeItem {
    String getItemKey();

    SourceIdentfier getSourceIdentitifer();

    EntityObject getPayload();

    ChangeItemState getState();

    void updateState(ChangeItemState state);

}
