package com.cumulativeminds.zeus.compute;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.compute.spi.ItemChunkProgressMonitor;
import com.cumulativeminds.zeus.core.spi.ItemChunk;

@Component
public class DataComuteProcessor {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private ExecutorService executor;
    private SimpleDataIndexer dataIndexer;

    @Inject
    public DataComuteProcessor(@Named("dataComputeProcessor") ExecutorService executorService,
            SimpleDataIndexer dataIndexer) {
        this.executor = executorService;
        this.dataIndexer = dataIndexer;
    }

    public void submit(ItemChunk chunk, ItemChunkProgressMonitor cb) {
        log.debug("new chunk submitted: {}", chunk);
        try {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        dataIndexer.process(chunk);
                        cb.onComplete(chunk);
                    } catch (Throwable ex) {
                        cb.onComplete(chunk, ex);
                    }
                }
            });
        } catch (RejectedExecutionException ex) {
            cb.onComplete(chunk, ex);
        }
    }
}
