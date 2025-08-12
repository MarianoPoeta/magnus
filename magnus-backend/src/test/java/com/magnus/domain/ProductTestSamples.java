package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Product getProductSample1() {
        return new Product().id(1L).name("name1").supplier("supplier1").leadTime(1).shelfLife(1);
    }

    public static Product getProductSample2() {
        return new Product().id(2L).name("name2").supplier("supplier2").leadTime(2).shelfLife(2);
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .supplier(UUID.randomUUID().toString())
            .leadTime(intCount.incrementAndGet())
            .shelfLife(intCount.incrementAndGet());
    }
}
