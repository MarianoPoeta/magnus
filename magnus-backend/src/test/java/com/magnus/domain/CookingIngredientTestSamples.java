package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CookingIngredientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CookingIngredient getCookingIngredientSample1() {
        return new CookingIngredient().id(1L).lastModifiedBy("lastModifiedBy1").version(1);
    }

    public static CookingIngredient getCookingIngredientSample2() {
        return new CookingIngredient().id(2L).lastModifiedBy("lastModifiedBy2").version(2);
    }

    public static CookingIngredient getCookingIngredientRandomSampleGenerator() {
        return new CookingIngredient()
            .id(longCount.incrementAndGet())
            .lastModifiedBy(UUID.randomUUID().toString())
            .version(intCount.incrementAndGet());
    }
}
