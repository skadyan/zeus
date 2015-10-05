package com.cumulativeminds.zeus.compute.spi;

import com.cumulativeminds.zeus.core.spi.ItemChunk;

public interface ItemChunkProgressMonitor {
    void onComplete(ItemChunk chunk);

    void onComplete(ItemChunk chunk, Throwable ex);
}
