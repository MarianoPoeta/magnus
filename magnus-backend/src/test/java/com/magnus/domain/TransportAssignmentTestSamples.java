package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TransportAssignmentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TransportAssignment getTransportAssignmentSample1() {
        return new TransportAssignment().id(1L).guestCount(1).pickupLocation("pickupLocation1").dropoffLocation("dropoffLocation1");
    }

    public static TransportAssignment getTransportAssignmentSample2() {
        return new TransportAssignment().id(2L).guestCount(2).pickupLocation("pickupLocation2").dropoffLocation("dropoffLocation2");
    }

    public static TransportAssignment getTransportAssignmentRandomSampleGenerator() {
        return new TransportAssignment()
            .id(longCount.incrementAndGet())
            .guestCount(intCount.incrementAndGet())
            .pickupLocation(UUID.randomUUID().toString())
            .dropoffLocation(UUID.randomUUID().toString());
    }
}
