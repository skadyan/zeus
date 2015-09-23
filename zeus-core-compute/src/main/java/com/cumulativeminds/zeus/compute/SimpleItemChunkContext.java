package com.cumulativeminds.zeus.compute;

import java.util.Iterator;
import java.util.List;

import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.spi.Item;
import com.cumulativeminds.zeus.core.spi.ProcessingContext;

public class SimpleItemChunkContext implements ProcessingContext {
    private final List<Item> items;
    private final Model model;

    public SimpleItemChunkContext(Model model, List<Item> items) {
        this.model = model;
        this.items = items;
    }

    @Override
    public Iterator<Item> iterator() {
        return items.iterator();
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public int size() {
        return items.size();
    }
}
