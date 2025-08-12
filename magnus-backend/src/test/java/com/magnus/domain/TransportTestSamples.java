package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TransportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Transport getTransportSample1() {
        return new Transport().id(1L).name("name1").capacity(1).fuelType("fuelType1");
    }

    public static Transport getTransportSample2() {
        return new Transport().id(2L).name("name2").capacity(2).fuelType("fuelType2");
    }

    public static Transport getTransportRandomSampleGenerator() {
        return new Transport()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .capacity(intCount.incrementAndGet())
            .fuelType(UUID.randomUUID().toString());
    }
}
