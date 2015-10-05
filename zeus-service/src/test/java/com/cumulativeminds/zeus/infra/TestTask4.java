package com.cumulativeminds.zeus.infra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.Member;
import com.hazelcast.core.MultiExecutionCallback;

import junit.framework.TestCase;

public class TestTask4 extends TestCase {

    private static final Logger logger = LoggerFactory.getLogger(TestTask4.class);

    private static final long timeoutMs = 3000;

    public void testTask4() throws Exception {
        Config config = new XmlConfigBuilder().build();
        config.setProperty("hazelcast.logging.type", "slf4j");
        // set call timeout to 3 seconds to make the problem appear quicker
        config.setProperty("hazelcast.operation.call.timeout.millis", String.valueOf(timeoutMs));

        // start two hazelcast instances
        HazelcastInstance hcInstance1 = Hazelcast.newHazelcastInstance(config);
        HazelcastInstance hcInstance2 = Hazelcast.newHazelcastInstance(config);
        logger.info("Hazelcast instances started.");

        // var to collect execution results
        List<String> results = new ArrayList<String>();

        // try-finally to stop hazelcast
        try {

            // create callback
            CountDownLatch latch = new CountDownLatch(1);
            Callback callback = new Callback(latch);

            // submit the long-running task
            logger.info("Executing task...");
            IExecutorService executorService = hcInstance1.getExecutorService("default");
            executorService.submitToAllMembers(new SleepingTask(), callback);
            logger.info("Task submitted.");

            // wait for all results
            logger.info("Waiting for results...");
            callback.await();
            for (Object value : callback.values.values()) {
                results.add(value.toString());
            }
            logger.info("Results received.");

        } catch (Exception e) {

            // log and rethrow exception
            logger.error("Exception: " + e.getMessage(), e);
            throw e;

        } finally {

            // shutdown
            hcInstance1.getLifecycleService().shutdown();
            hcInstance2.getLifecycleService().shutdown();
        }

        // ensure success
        for (String result : results) {
            if (!result.equals("Success")) {
                fail("Non-success result: " + result);
            }
        }

        logger.info("Test done.");
    }

    private static class SleepingTask implements Callable<String>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public String call() throws Exception {
            Thread.sleep(15000);
            return "Success";
        }

    }

    private static class Callback implements MultiExecutionCallback {

        private final CountDownLatch latch;

        private Map<Member, Object> values;

        public Callback(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onResponse(Member member, Object value) {
            logger.info("Member " + member + " done: " + value);
        }

        @Override
        public void onComplete(Map<Member, Object> values) {
            this.values = values;
            logger.info("All members done: " + values.values());
            latch.countDown();
        }

        public void await() throws InterruptedException {
            latch.await();
        }

    }

}