package com.cumulativeminds.zeus.api.internal;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import com.cumulativeminds.zeus.ModelSettings;
import com.cumulativeminds.zeus.compute.ChangeTask;
import com.cumulativeminds.zeus.core.meta.Model;

public class ChangeRequestThreadPoolExecutor extends ThreadPoolExecutor {

    private static final String CONTEXT = "changeRequestThreadPoolExecutor";

    private ChangeRequestThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static ChangeRequestThreadPoolExecutor newExecutor(Model model, ModelSettings settings) {
        int poolSize = settings.getSetting(model, CONTEXT, "poolSize", Integer.class, 4);
        TimeUnit keepAliveTimeUnit = settings.getSetting(model, CONTEXT, "keepAliveTimeUnit", TimeUnit.class, TimeUnit.SECONDS);
        int keepAliveTime = settings.getSetting(model, CONTEXT, "keepAliveTime", Integer.class, 0);
        int queueCapacity = settings.getSetting(model, CONTEXT, "queueCapacity", Integer.class, 100);
        BlockingQueue<Runnable> workQueue = newPriorityAndCapacityBlockingQueue(queueCapacity);
        ChangeRequestThreadPoolExecutor executor = new ChangeRequestThreadPoolExecutor(
                poolSize,
                poolSize,
                keepAliveTime,
                keepAliveTimeUnit,
                workQueue,
                threadFactory(model.getCode()),
                new AbortPolicy());
        return executor;

    }

    private static ThreadFactory threadFactory(String code) {
        return new CustomizableThreadFactory("req-" + code + "-in");
    }

    @SuppressWarnings("serial")
    private static BlockingQueue<Runnable> newPriorityAndCapacityBlockingQueue(int queueCapacity) {
        return new PriorityBlockingQueue<Runnable>(100, PriorityComparator.INSTANCE) {
            @Override
            public boolean offer(Runnable e) {
                if (size() >= queueCapacity) {
                    return false;
                }
                return super.offer(e);
            }

        };
    }

    static class PriorityComparator implements Comparator<Runnable> {
        public static final Comparator<Runnable> INSTANCE = new PriorityComparator();

        @Override
        public int compare(Runnable o1, Runnable o2) {
            int p1 = ((ChangeTask) o1).getPriority();
            int p2 = ((ChangeTask) o2).getPriority();

            return p1 - p2;
        }

    }

}
