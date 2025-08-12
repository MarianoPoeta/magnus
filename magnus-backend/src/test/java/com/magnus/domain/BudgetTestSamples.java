package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BudgetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Budget getBudgetSample1() {
        return new Budget()
            .id(1L)
            .name("name1")
            .clientName("clientName1")
            .eventLocation("eventLocation1")
            .guestCount(1)
            .templateId("templateId1")
            .version(1)
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Budget getBudgetSample2() {
        return new Budget()
            .id(2L)
            .name("name2")
            .clientName("clientName2")
            .eventLocation("eventLocation2")
            .guestCount(2)
            .templateId("templateId2")
            .version(2)
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Budget getBudgetRandomSampleGenerator() {
        return new Budget()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .clientName(UUID.randomUUID().toString())
            .eventLocation(UUID.randomUUID().toString())
            .guestCount(intCount.incrementAndGet())
            .templateId(UUID.randomUUID().toString())
            .version(intCount.incrementAndGet())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
