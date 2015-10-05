package com.cumulativeminds.zeus.compute.internal;

import java.util.Iterator;
import java.util.List;

import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.spi.Item;
import com.cumulativeminds.zeus.core.spi.ItemChunk;

public class ItemChunkImpl implements ItemChunk {
    private transient Model model;
    private List<Item> items;
    private Integer chunkId;
    private Integer batchId;
    private boolean halted;

    public ItemChunkImpl(Integer chunkId, Integer batchId, Model model, List<Item> items) {
        this.chunkId = chunkId;
        this.batchId = batchId;
        this.items = items;

        this.setModel(model);
    }

    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public Iterator<Item> iterator() {
        return items.iterator();
    }

    @Override
    public Integer batchId() {
        return batchId;
    }

    @Override
    public boolean isHalted() {
        return halted;
    }

    @Override
    public Integer chunkId() {
        return chunkId;
    }

    @Override
    public String toString() {
        return String.format("ChunkOf(%s)@[%s,%s]", model, batchId, chunkId);
    }

    @Override
    public void halt() {
        halted = true;
    }
}
