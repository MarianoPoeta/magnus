package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConflictResolutionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ConflictResolution getConflictResolutionSample1() {
        return new ConflictResolution()
            .id(1L)
            .entityType("entityType1")
            .entityId("entityId1")
            .fieldName("fieldName1")
            .resolutionStrategy("resolutionStrategy1");
    }

    public static ConflictResolution getConflictResolutionSample2() {
        return new ConflictResolution()
            .id(2L)
            .entityType("entityType2")
            .entityId("entityId2")
            .fieldName("fieldName2")
            .resolutionStrategy("resolutionStrategy2");
    }

    public static ConflictResolution getConflictResolutionRandomSampleGenerator() {
        return new ConflictResolution()
            .id(longCount.incrementAndGet())
            .entityType(UUID.randomUUID().toString())
            .entityId(UUID.randomUUID().toString())
            .fieldName(UUID.randomUUID().toString())
            .resolutionStrategy(UUID.randomUUID().toString());
    }
}
