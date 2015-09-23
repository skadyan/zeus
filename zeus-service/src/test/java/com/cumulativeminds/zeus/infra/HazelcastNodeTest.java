package com.cumulativeminds.zeus.infra;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.Test;

import com.cumulativeminds.zeus.ServiceTestCase;
import com.cumulativeminds.zeus.infra.tasks.ExecutorStats;

public class HazelcastNodeTest extends ServiceTestCase {
    static enum DistributedObjNames implements DistributedObjectName {
        workService
    }

    @Inject
    private HazelcastNode node;

    @Test
    public void startNodeLocal() throws Exception {
        ExecutorStats stats = node.getExecutorStats(DistributedObjNames.workService);
        assertThat(stats.getStartedTaskCount(), is(0L));
        String groupName = node.getConfig().getGroupConfig().getName();

        assertThat(groupName, is("zeus-dev"));
    }
}
