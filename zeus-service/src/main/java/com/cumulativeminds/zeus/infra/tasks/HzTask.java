package com.cumulativeminds.zeus.infra.tasks;

import java.io.Serializable;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

import com.cumulativeminds.zeus.infra.HazelcastNode;
import com.cumulativeminds.zeus.infra.HazelcastNodeFactoryBean;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;

public abstract class HzTask<V> implements Callable<V>, HazelcastInstanceAware, Serializable {
    private static final long serialVersionUID = -5564351566981781349L;
    protected static final Logger log = LoggerFactory.getLogger(HzTask.class);
    protected transient BeanFactory beanFactory;
    protected transient HazelcastNode hazelcastNode;

    protected HzTask() {
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        BeanFactory beanFactory = (BeanFactory) hazelcastInstance.getUserContext().get(HazelcastNodeFactoryBean.BEAN_FACTORY);
        
        this.hazelcastNode = beanFactory.getBean(HazelcastNode.class);
        this.beanFactory = beanFactory;
    }
}
