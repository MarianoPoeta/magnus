package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TaskDependencyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TaskDependency getTaskDependencySample1() {
        return new TaskDependency().id(1L).notes("notes1");
    }

    public static TaskDependency getTaskDependencySample2() {
        return new TaskDependency().id(2L).notes("notes2");
    }

    public static TaskDependency getTaskDependencyRandomSampleGenerator() {
        return new TaskDependency().id(longCount.incrementAndGet()).notes(UUID.randomUUID().toString());
    }
}
