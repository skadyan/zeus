package com.cumulativeminds.zeus.compute;

import com.cumulativeminds.zeus.core.spi.Change;

public interface DataProcessorFacade {
    void submit(Change change);
}
