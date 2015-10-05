package com.cumulativeminds.zeus.infra;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;

import junit.framework.TestCase;

public class TestTask2 extends TestCase {

    private static final Logger logger = Logger.getLogger(TestTask2.class);

    public void testTask2() throws Exception {

        // create hazelcast config
        Config config = new XmlConfigBuilder().build();
        config.setProperty("hazelcast.logging.type", "slf4j");

        // set call timeout to 1 second to make the problem appear quicker
        config.setProperty("hazelcast.operation.call.timeout.millis", "2000");

        // create two Hazelcast instances
        HazelcastInstance hcInstance1 = Hazelcast.newHazelcastInstance(config);
        HazelcastInstance hcInstance2 = Hazelcast.newHazelcastInstance(config);
        try {

            // submit the long-running task
            IExecutorService executorService = hcInstance1.getExecutorService("default");
            Map<Member, Future<Object>> futures = executorService.submitToAllMembers(new SleepingTask());

            // call get on the "remote" member
            Member remoteMember = hcInstance2.getCluster().getLocalMember();
            // wait for 5 seconds
            // when we call get on the future after sleeping, the
            // OperationTimeoutException will be raised in Hazelcast
            // 3.4.1-20141222 dev
            Thread.sleep(8000);
            String result = (String) futures.get(remoteMember).get();

            assertEquals("Success", result);

        } finally {
            // shutdown Hazelcast instances
            hcInstance1.getLifecycleService().shutdown();
            hcInstance2.getLifecycleService().shutdown();
        }

        logger.info("Test done.");
    }

    private static class SleepingTask implements Callable<Object>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public Object call() throws Exception {
            Thread.sleep(6000);
            return "Success";
        }

    }

}