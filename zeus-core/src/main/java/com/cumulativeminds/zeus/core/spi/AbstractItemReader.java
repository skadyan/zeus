package com.cumulativeminds.zeus.core.spi;

import java.util.Map;

import com.cumulativeminds.zeus.core.meta.Model;

public abstract class AbstractItemReader implements ItemReader {
    protected final Model model;
    protected final Map<String, Object> arguments;

    public AbstractItemReader(Model model, Map<String, Object> arguments) {
        this.model = model;
        this.arguments = arguments;
    }

    @Override
    public void initialize() {
    }

}
