package com.cumulativeminds.zeus.core.spi;

import org.springframework.core.Ordered;

public interface ChunkHandler extends Ordered {
    void process(ProcessingContext context);
}
