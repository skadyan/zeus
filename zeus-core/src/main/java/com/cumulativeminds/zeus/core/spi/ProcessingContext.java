package com.cumulativeminds.zeus.core.spi;

import com.cumulativeminds.zeus.core.meta.Model;

public interface ProcessingContext extends Iterable<Item> {
    Model getModel();
    int size();
    boolean isHalted();
    void halt();
}
