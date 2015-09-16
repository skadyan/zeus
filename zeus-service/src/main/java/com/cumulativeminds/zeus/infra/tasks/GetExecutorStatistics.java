package com.cumulativeminds.zeus.infra.tasks;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import com.cumulativeminds.zeus.infra.DistributedObjectName;
import com.hazelcast.monitor.LocalExecutorStats;

public class GetExecutorStatistics extends HzTask<ExecutorStats> {
    private static final long serialVersionUID = -2410393619964384742L;
    private DistributedObjectName serviceName;

    private GetExecutorStatistics(DistributedObjectName serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public ExecutorStats call() throws Exception {
        LocalExecutorStats ls = hazelcastNode.getGlobalExecutor(serviceName).getLocalExecutorStats();

        ExecutorStats stats = new ExecutorStats();
        stats.setCancelledTaskCount(ls.getCancelledTaskCount());
        stats.setCompletedTaskCount(ls.getCompletedTaskCount());
        stats.setCreationTime(LocalDateTime.now());
        stats.setPendingTaskCount(ls.getPendingTaskCount());
        stats.setStartedTaskCount(ls.getStartedTaskCount());
        stats.setTotalExecutionLatency(ls.getTotalExecutionLatency());
        stats.setTotalStartLatency(ls.getTotalStartLatency());

        return stats;
    }

    public static Callable<ExecutorStats> of(DistributedObjectName name) {
        return new GetExecutorStatistics(name);
    }
}
