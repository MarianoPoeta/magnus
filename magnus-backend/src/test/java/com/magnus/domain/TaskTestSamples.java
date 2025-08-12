package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TaskTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Task getTaskSample1() {
        return new Task()
            .id(1L)
            .title("title1")
            .estimatedDuration(1)
            .actualDuration(1)
            .location("location1")
            .invoiceUrl("invoiceUrl1")
            .parentTaskId("parentTaskId1")
            .version(1)
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Task getTaskSample2() {
        return new Task()
            .id(2L)
            .title("title2")
            .estimatedDuration(2)
            .actualDuration(2)
            .location("location2")
            .invoiceUrl("invoiceUrl2")
            .parentTaskId("parentTaskId2")
            .version(2)
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Task getTaskRandomSampleGenerator() {
        return new Task()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .estimatedDuration(intCount.incrementAndGet())
            .actualDuration(intCount.incrementAndGet())
            .location(UUID.randomUUID().toString())
            .invoiceUrl(UUID.randomUUID().toString())
            .parentTaskId(UUID.randomUUID().toString())
            .version(intCount.incrementAndGet())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
