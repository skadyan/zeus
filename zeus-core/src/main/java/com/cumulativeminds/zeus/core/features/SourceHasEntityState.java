package com.cumulativeminds.zeus.core.features;

import com.cumulativeminds.zeus.core.spi.ItemStateHandler;
import com.cumulativeminds.zeus.core.spi.ProcessingContext;

public class SourceHasEntityState implements Feature {
    public static final String NAME = SourceHasEntityState.class.getSimpleName();
    private ItemStateHandler handler;

    public SourceHasEntityState() {
    }

    public void apply(ProcessingContext context) {
        handler.process(context);
    }
}
