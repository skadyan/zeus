package com.cumulativeminds.zeus.core.meta;

public interface ModelRegistry extends Iterable<Model> {
    Model getModelByCode(String code);

    Model getModelBySource(String source);

    boolean isRegistered(String code);

    void register(Model model);

}
