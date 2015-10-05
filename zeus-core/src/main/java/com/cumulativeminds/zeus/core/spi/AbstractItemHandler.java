package com.cumulativeminds.zeus.core.spi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

public abstract class AbstractItemHandler implements ChunkHandler {
    protected final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    public AbstractItemHandler() {
    }

    @Override
    public int getOrder() {
        int order = Ordered.HIGHEST_PRECEDENCE;
        Order annotation = AnnotationUtils.getAnnotation(getClass(), Order.class);
        if (annotation != null) {
            order = annotation.value();
        }

        return order;
    }

    public List<Item> filterEligibleItems(ProcessingContext context, ItemState forState) {
        List<Item> items = new ArrayList<>(context.size());
        for (Item item : context) {
            if (isEligibleItem(item)) {
                items.add(item);
            }
        }

        return items;
    }

    protected boolean isEligibleItem(Item item) {
        return true;
    }
}
