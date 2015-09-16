package com.cumulativeminds.zeus.infra;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cumulativeminds.zeus.infra.tasks.ExecutorStats;
import com.cumulativeminds.zeus.infra.tasks.GetExecutorStatistics;
import com.hazelcast.config.Config;
import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.ILock;
import com.hazelcast.core.Member;
import com.hazelcast.core.PartitionService;

@Component
public class HazelcastNode {
    private static final Logger log = LoggerFactory.getLogger(HazelcastNode.class);
    public static final String NAME = "HzNode";

    private Config config;

    private HazelcastInstance hazelcastInstance;

    private Map<DistributedObjectName, IExecutorService> freqUsedExecutors;
    private Map<DistributedObjectName, ILock> freqUsedLocks;

    private final DistributedObjectName SHARED_GLOBAL_EXECUTOR;

    @Inject
    public HazelcastNode(Config config) {
        this.config = config;
        this.freqUsedExecutors = new HashMap<>();
        this.freqUsedLocks = new HashMap<>();
        SHARED_GLOBAL_EXECUTOR = () -> "SHARED_GLOBAL_EXECUTOR";
    }

    @PostConstruct
    public void init() {
        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        // associate this with user-context
        hazelcastInstance.getUserContext().put(NAME, this);

        log.info("Hazelcast Node created successfully");

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

    @PreDestroy
    public void destory() {
        PartitionService partitionService = hazelcastInstance.getPartitionService();
        boolean isSafe = partitionService.isLocalMemberSafe();
        if (isSafe) {
            hazelcastInstance.shutdown();
        } else {
            int seconds = getConfigAsInt("forceLocalMemberToBeSafe.timeout", 0);
            boolean safe = partitionService.forceLocalMemberToBeSafe(seconds, TimeUnit.SECONDS);
            log.info("Local member was forced to be safe with timeout of {} seconds. Is Local member Safe: {}", seconds, safe);
            hazelcastInstance.shutdown();
        }
    }

    private int getConfigAsInt(String name, int defaultValue) {
        String value = config.getProperty(name);
        int typedValue = defaultValue;
        if (StringUtils.hasText(value)) {
            typedValue = Integer.parseInt(value);
        }

        return typedValue;
    }

    public Lock getDistributedLock(DistributedObjectName name) {
        return reuseOrGetLockFromHazelcastNode(name);
    }

    public ExecutorStats getExecutorStats(DistributedObjectName service) {
        int timeout = getConfigAsInt("sharedGlobalTask.timeout", 30);
        IdentityHashMap<Member, ExecutorStats> results = new IdentityHashMap<>();
        getSharedGlobalExecutor().submitToAllMembers(GetExecutorStatistics.of(service))
                .forEach((m, f) -> {
                    try {
                        ExecutorStats executorStats = f.get(timeout, TimeUnit.SECONDS);
                        results.put(m, executorStats);
                    } catch (Exception e) {
                        log.warn("Unable to get '{}' executor stats from: {}", service.name(), m);
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

}
