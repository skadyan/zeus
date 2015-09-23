package com.cumulativeminds.zeus.core.spi;

import java.time.LocalDateTime;

public interface Item {
    /* key must be URL friendly in order o save URI encode/decode */
    String getItemKey();

    SourceKey getSourceKey();

    EntityObject getPayload();

    Comparable<?> version();

    Comparable<?> scn();

    ItemState getState();

    LocalDateTime getEffectiveDate();

    LocalDateTime getLastModified();

    void updateState(ItemState state);

}
