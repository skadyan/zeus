package com.cumulativeminds.zeus.infra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

/**
 * Test if a task that runs longer than the call timeout is executed correctly when get is called
 * on the future after at least 2 times the call timeout has elapsed.
 * <p>
 * See https://github.com/hazelcast/hazelcast/issues/4398
 */
public class TestTask3 extends TestCase {

    private static final Logger logger = Logger.getLogger(TestTask3.class);

    private static final long timeoutMs = 3000;
 
    public void testTask3NoSleep() throws Exception {
        testTask3(false, false);
    }

    public void testTask3NoSleepReverse() throws Exception {
        testTask3(false, true);
    }

    public void testTask3Sleep() throws Exception {
        testTask3(true, false);
    }

    public void testTask3SleepReverse() throws Exception {
        testTask3(true, true);
    }

    private void testTask3(boolean sleep, boolean reverse) throws Exception {

        // create hazelcast config
        Config config = new XmlConfigBuilder().build();
        config.setProperty("hazelcast.logging.type", "log4j");

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

            // submit the long-running task
            logger.info("Executing task...");
            IExecutorService executorService = hcInstance1.getExecutorService("default");
            Map<Member, Future<String>> futures = executorService.submitToAllMembers(new SleepingTask());
            logger.info("Task submitted.");

            // sleep before calling Future.get() (2 x timeout + 2s to be after timeout)
            if (sleep) {
                Thread.sleep(2 * timeoutMs + 2000);
            }

            // wait for all results
            logger.info("Waiting for futures...");
            List<Future<String>> futuresList = new ArrayList<Future<String>>(futures.values());
            if (reverse) {
                Collections.reverse(futuresList);
            }
            for (Future<String> future : futuresList) {
                String result = future.get();
                results.add(result);
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

}