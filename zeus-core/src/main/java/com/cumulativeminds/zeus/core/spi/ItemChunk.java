package com.cumulativeminds.zeus.core.spi;

public interface ItemChunk extends ProcessingContext {
    Number batchId();

    Number chunkId();

}
