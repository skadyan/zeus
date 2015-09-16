package com.cumulativeminds.zeus.infra.tasks;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ExecutorStats implements Serializable {
    private static final long serialVersionUID = 1152763540073304323L;

    private LocalDateTime creationTime;
    private long pendingTaskCount;
    private long startedTaskCount;
    private long completedTaskCount;
    private long cancelledTaskCount;
    private long totalStartLatency;
    private long totalExecutionLatency;

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public long getPendingTaskCount() {
        return pendingTaskCount;
    }

    public void setPendingTaskCount(long pendingTaskCount) {
        this.pendingTaskCount = pendingTaskCount;
    }

    public long getStartedTaskCount() {
        return startedTaskCount;
    }

    public void setStartedTaskCount(long startedTaskCount) {
        this.startedTaskCount = startedTaskCount;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public long getCancelledTaskCount() {
        return cancelledTaskCount;
    }

    public void setCancelledTaskCount(long cancelledTaskCount) {
        this.cancelledTaskCount = cancelledTaskCount;
    }

    public long getTotalStartLatency() {
        return totalStartLatency;
    }

    public void setTotalStartLatency(long totalStartLatency) {
        this.totalStartLatency = totalStartLatency;
    }

    public long getTotalExecutionLatency() {
        return totalExecutionLatency;
    }

    public void setTotalExecutionLatency(long totalExecutionLatency) {
        this.totalExecutionLatency = totalExecutionLatency;
    }

    public void mergeFrom(ExecutorStats r) {
        pendingTaskCount += r.pendingTaskCount;
        startedTaskCount += r.startedTaskCount;
        completedTaskCount += r.completedTaskCount;
        cancelledTaskCount += r.cancelledTaskCount;
        totalStartLatency += r.totalStartLatency;
        totalExecutionLatency += r.totalExecutionLatency;
    }
}
