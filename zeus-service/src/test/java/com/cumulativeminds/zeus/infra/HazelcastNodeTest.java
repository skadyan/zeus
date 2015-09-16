package com.cumulativeminds.zeus.infra;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cumulativeminds.zeus.infra.tasks.ExecutorStats;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { HazelcastTestConfig.class })
public class HazelcastNodeTest {
    static enum DistributedObjNames implements DistributedObjectName {
        workService
    }

    @Inject
    private HazelcastNode node;

    @Test
    public void startNodeLocal() throws Exception {
        ExecutorStats stats = node.getExecutorStats(DistributedObjNames.workService);
        assertThat(stats.getStartedTaskCount(), is(0L));
    }
}
