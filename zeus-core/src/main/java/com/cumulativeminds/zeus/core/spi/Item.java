package com.cumulativeminds.zeus.core.spi;

import java.time.LocalDateTime;

public interface Item {
    /* key must be URL friendly in order o save URI encode/decode */
    String getItemKey();

    SourceKey getSourceKey();

    EntityObject getPayload();

    Integer version();

    String scn();

    ItemState getState();

    LocalDateTime getEffectiveDate();

    LocalDateTime getLastUpdated();

    void state(ItemState state);

    void lastUpdated(LocalDateTime lastModified);

    void scn(String scn);

}
