package com.cumulativeminds.zeus.infra.tasks;

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cumulativeminds.zeus.infra.HazelcastNode;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;

public abstract class HzTask<V> implements Callable<V>, HazelcastInstanceAware, Serializable {
    private static final long serialVersionUID = -5564351566981781349L;
    protected transient final Logger log = LoggerFactory.getLogger(getClass());
    protected transient HazelcastNode hazelcastNode;

    protected HzTask() {
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        hazelcastNode = (HazelcastNode) hazelcastInstance.getUserContext().get(HazelcastNode.NAME);
    }
}
