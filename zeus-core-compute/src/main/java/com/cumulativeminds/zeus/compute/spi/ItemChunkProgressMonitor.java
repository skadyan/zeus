package com.cumulativeminds.zeus.compute.spi;

import com.cumulativeminds.zeus.core.spi.Item;
import com.cumulativeminds.zeus.core.spi.ItemChunk;

public interface ItemChunkProgressMonitor {
    void onItemProcessed(Item item);

    void onComplete(ItemChunk chunk);
}
