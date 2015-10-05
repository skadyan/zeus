package com.cumulativeminds.zeus.core.spi;

public interface ItemReaderCallback {
    void onItem(EntityObject payload);
    void done();
    boolean isHalted();
}
