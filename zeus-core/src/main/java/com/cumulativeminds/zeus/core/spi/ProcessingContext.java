package com.cumulativeminds.zeus.core.spi;

import com.cumulativeminds.zeus.core.meta.Model;

public interface ProcessingContext extends Iterable<ChangeItem> {
    Model getModel();

    int size();

}
