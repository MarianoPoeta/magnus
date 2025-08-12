package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WorkflowTriggerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static WorkflowTrigger getWorkflowTriggerSample1() {
        return new WorkflowTrigger()
            .id(1L)
            .triggerName("triggerName1")
            .entityType("entityType1")
            .actionType("actionType1")
            .executionOrder(1)
            .executionCount(1);
    }

    public static WorkflowTrigger getWorkflowTriggerSample2() {
        return new WorkflowTrigger()
            .id(2L)
            .triggerName("triggerName2")
            .entityType("entityType2")
            .actionType("actionType2")
            .executionOrder(2)
            .executionCount(2);
    }

    public static WorkflowTrigger getWorkflowTriggerRandomSampleGenerator() {
        return new WorkflowTrigger()
            .id(longCount.incrementAndGet())
            .triggerName(UUID.randomUUID().toString())
            .entityType(UUID.randomUUID().toString())
            .actionType(UUID.randomUUID().toString())
            .executionOrder(intCount.incrementAndGet())
            .executionCount(intCount.incrementAndGet());
    }
}
