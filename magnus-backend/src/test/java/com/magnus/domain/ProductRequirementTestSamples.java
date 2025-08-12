package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductRequirementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductRequirement getProductRequirementSample1() {
        return new ProductRequirement().id(1L).purchasedBy("purchasedBy1").version(1);
    }

    public static ProductRequirement getProductRequirementSample2() {
        return new ProductRequirement().id(2L).purchasedBy("purchasedBy2").version(2);
    }

    public static ProductRequirement getProductRequirementRandomSampleGenerator() {
        return new ProductRequirement()
            .id(longCount.incrementAndGet())
            .purchasedBy(UUID.randomUUID().toString())
            .version(intCount.incrementAndGet());
    }
}
