package com.cumulativeminds.zeus.compute;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.core.spi.ChunkHandler;
import com.cumulativeminds.zeus.core.spi.DataIndexer;
import com.cumulativeminds.zeus.core.spi.ProcessingContext;

@Component
public class SimpleDataIndexer implements DataIndexer {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private List<ChunkHandler> handlers;

    @Inject
    public SimpleDataIndexer(List<ChunkHandler> handlers) {
        this.handlers = validateAndSort(handlers);
        log.info("Following handlers are setup:");
        for (ChunkHandler chunkHandler : handlers) {
            log.info(" - {}", chunkHandler);
        }
    }

    private List<ChunkHandler> validateAndSort(List<ChunkHandler> handlers) {
        Collections.sort(handlers, OrderComparator.INSTANCE);

        ChunkHandler lastHandler = null;
        int last = Ordered.LOWEST_PRECEDENCE;

        for (ChunkHandler chunkHandler : handlers) {
            if (last == chunkHandler.getOrder()) {
                throw new IllegalStateException("Order must be explicitly provided for each chunk "
                        + "handler. Two handler can't have same ordering value. Offending handlers are: " + lastHandler + " and "
                        + chunkHandler);
            }

            last = chunkHandler.getOrder();
            lastHandler = chunkHandler;
        }

        return handlers;
    }

    @Override
    public void process(ProcessingContext context) {
        List<ChunkHandler> handlers = this.handlers;
        for (ChunkHandler handler : handlers) {
            handler.process(context);
            if (context.isHalted()) {
                break;
            }
        }
    }
}
