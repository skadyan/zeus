package com.cumulativeminds.zeus.core.spi;

public interface ChangeItemChunk extends ProcessingContext {
    Number batchId();

    Number chunkId();

}
