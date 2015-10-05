package com.cumulativeminds.zeus.compute;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cumulativeminds.zeus.compute.internal.ItemChunkImpl;
import com.cumulativeminds.zeus.compute.internal.ItemImpl;
import com.cumulativeminds.zeus.compute.spi.ItemChunkProgressMonitor;
import com.cumulativeminds.zeus.core.meta.Model;
import com.cumulativeminds.zeus.core.spi.EntityObject;
import com.cumulativeminds.zeus.core.spi.Item;
import com.cumulativeminds.zeus.core.spi.ItemChunk;
import com.cumulativeminds.zeus.core.spi.ItemReader;
import com.cumulativeminds.zeus.core.spi.ItemReaderCallback;

@Component
@Scope(SCOPE_PROTOTYPE)
public class ChangeTask implements Runnable, ItemReaderCallback, ItemChunkProgressMonitor {
    private static volatile int idGen = 0;

    private int taskId = ++idGen;
    private Model model;
    private ItemReader reader;

    private int priority;

    private List<Item> items = new ArrayList<>();

    @Value("${indexer.chunkSize}")
    private int chunkSize;

    @Inject
    private DataComuteProcessor comuteProcessor;

    private boolean halted;

    public ChangeTask(Model model, int priority, ItemReader reader) {
        this.model = model;
        this.priority = priority;
        this.reader = reader;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    @Override
    public void run() {
        reader.initialize();
        reader.read(this);
    }

    public int getPriority() {
        return priority != 0 ? priority : taskId;
    }

    @Override
    public void onItem(EntityObject payload) {
        ItemImpl item = new ItemImpl(model, payload);
        items.add(item);
        if (items.size() == chunkSize) {
            submitNewChunk();
        }
    }

    private void submitNewChunk() {
        ItemChunkImpl chunk = new ItemChunkImpl(newChunkId(), taskId, model, items);
        comuteProcessor.submit(chunk, this);
    }

    private int newChunkId() {
        return idGen++;
    }

    @Override
    public void done() {
        if (items.size() > 0) {
            submitNewChunk();
        }
    }

    @Override
    public void onComplete(ItemChunk chunk) {
    }

    @Override
    public void onComplete(ItemChunk chunk, Throwable ex) {
        halted = chunk.isHalted();
    }

    @Override
    public boolean isHalted() {
        return halted;
    }
}
