package com.cumulativeminds.zeus.infra;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulativeminds.zeus.infra.tasks.ExecutorStats;
import com.cumulativeminds.zeus.infra.tasks.GetExecutorStatistics;
import com.hazelcast.config.Config;
import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.ILock;
import com.hazelcast.core.Member;
import com.hazelcast.core.PartitionService;

public class HazelcastNode {
    private static final Logger log = LoggerFactory.getLogger(HazelcastNode.class);
    private final DistributedObjectName SHARED_GLOBAL_EXECUTOR = () -> "SHARED_GLOBAL_EXECUTOR";

    private HazelcastInstance hazelcastInstance;

    private Map<DistributedObjectName, IExecutorService> freqUsedExecutors;
    private Map<DistributedObjectName, ILock> freqUsedLocks;
    private Map<String, Object> settings;

    public HazelcastNode(HazelcastInstance hazelcastInstance, Map<String, Object> settings) {
        this.hazelcastInstance = hazelcastInstance;
        this.settings = settings;
        this.freqUsedExecutors = new HashMap<>();
        this.freqUsedLocks = new HashMap<>();
    }

    public <T> Future<T> runOnClustor(DistributedObjectName service, Callable<T> task) {
        return reuseOrGetExecutorServiceFromHazelcastNode(service).submit(task);
    }

    public <T> void runOnClustor(DistributedObjectName service, Callable<T> task, ExecutionCallback<T> callback) {
        reuseOrGetExecutorServiceFromHazelcastNode(service).submit(task, callback);
    }

    private IExecutorService reuseOrGetExecutorServiceFromHazelcastNode(DistributedObjectName service) {
        Map<DistributedObjectName, IExecutorService> cached = freqUsedExecutors;
        IExecutorService executorService = cached.get(service);
        if (executorService == null) {
            synchronized (this) {
                executorService = cached.get(service);
                if (executorService == null) {
                    executorService = hazelcastInstance.getExecutorService(service.name());
                    cached.put(service, executorService);
                }
            }
        }

        return executorService;
    }

    private ILock reuseOrGetLockFromHazelcastNode(DistributedObjectName name) {
        Map<DistributedObjectName, ILock> cached = freqUsedLocks;
        ILock lock = cached.get(name);
        if (lock == null) {
            synchronized (this) {
                lock = cached.get(name);
                if (lock == null) {
                    hazelcastInstance.getExecutorService(name.name());
                    cached.put(name, lock);
                }
            }
        }

        return lock;
    }

    public void destory() {
        PartitionService partitionService = hazelcastInstance.getPartitionService();
        boolean isSafe = partitionService.isLocalMemberSafe();
        if (isSafe) {
            hazelcastInstance.shutdown();
            log.info("Hazelcast is shutdown now");
        } else {
            int seconds = getConfigAsInt("forceLocalMemberToBeSafeTimeoutSeconds", 0);
            boolean safe = partitionService.forceLocalMemberToBeSafe(seconds, TimeUnit.SECONDS);
            log.info("Local member was forced to be safe with timeout of {} seconds. Is Local member Safe: {}", seconds, safe);
            hazelcastInstance.shutdown();
        }
    }

    private int getConfigAsInt(String name, int defaultValue) {
        Number number = (Number) settings.get(name);
        int typedValue = defaultValue;
        if (number != null) {
            typedValue = number.intValue();
        }

        return typedValue;
    }

    public Lock getDistributedLock(DistributedObjectName name) {
        return reuseOrGetLockFromHazelcastNode(name);
    }

    public ExecutorStats getExecutorStats(DistributedObjectName service) {
        int timeout = getConfigAsInt("sharedGlobalTaskTimeoutSeconds", 30);
        IdentityHashMap<Member, ExecutorStats> results = new IdentityHashMap<>();
        getSharedGlobalExecutor().submitToAllMembers(GetExecutorStatistics.of(service))
                .forEach((m, f) -> {
                    try {
                        ExecutorStats executorStats = f.get(timeout, TimeUnit.SECONDS);
                        results.put(m, executorStats);
                    } catch (Exception e) {
                        log.warn("Unable to get '{}' executor stats from: {}. - {}", service.name(), m, e);
                    }
                });
        ExecutorStats stats = new ExecutorStats();
        results.values().forEach(r -> stats.mergeFrom(r));
        return stats;
    }

    public IExecutorService getSharedGlobalExecutor() {
        return reuseOrGetExecutorServiceFromHazelcastNode(SHARED_GLOBAL_EXECUTOR);
    }

    public IExecutorService getGlobalExecutor(DistributedObjectName service) {
        return reuseOrGetExecutorServiceFromHazelcastNode(service);
    }

    public String getLocalEndpoint() {
        return hazelcastInstance.getLocalEndpoint().toString();
    }

    public Config getConfig() {
        return hazelcastInstance.getConfig();
    }

}
