package com.cumulativeminds.zeus.core.spi;

public interface ItemChunk extends ProcessingContext {
    Integer batchId();

    Integer chunkId();

}
