package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AccommodationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Accommodation getAccommodationSample1() {
        return new Accommodation().id(1L).name("name1").maxOccupancy(1).checkInTime("checkInTime1").checkOutTime("checkOutTime1");
    }

    public static Accommodation getAccommodationSample2() {
        return new Accommodation().id(2L).name("name2").maxOccupancy(2).checkInTime("checkInTime2").checkOutTime("checkOutTime2");
    }

    public static Accommodation getAccommodationRandomSampleGenerator() {
        return new Accommodation()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .maxOccupancy(intCount.incrementAndGet())
            .checkInTime(UUID.randomUUID().toString())
            .checkOutTime(UUID.randomUUID().toString());
    }
}
