package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NeedTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Need getNeedSample1() {
        return new Need().id(1L).description("description1").quantity(1).unit("unit1");
    }

    public static Need getNeedSample2() {
        return new Need().id(2L).description("description2").quantity(2).unit("unit2");
    }

    public static Need getNeedRandomSampleGenerator() {
        return new Need()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet())
            .unit(UUID.randomUUID().toString());
    }
}
