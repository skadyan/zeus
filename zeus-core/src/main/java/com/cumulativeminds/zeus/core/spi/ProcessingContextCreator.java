package com.cumulativeminds.zeus.core.spi;

public interface ProcessingContextCreator {
    /**
     * @return Null if nothing to work.
     */
    ProcessingContext next();
}
