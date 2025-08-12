package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ActivityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Activity getActivitySample1() {
        return new Activity().id(1L).name("name1").duration(1).maxCapacity(1).location("location1");
    }

    public static Activity getActivitySample2() {
        return new Activity().id(2L).name("name2").duration(2).maxCapacity(2).location("location2");
    }

    public static Activity getActivityRandomSampleGenerator() {
        return new Activity()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .duration(intCount.incrementAndGet())
            .maxCapacity(intCount.incrementAndGet())
            .location(UUID.randomUUID().toString());
    }
}
