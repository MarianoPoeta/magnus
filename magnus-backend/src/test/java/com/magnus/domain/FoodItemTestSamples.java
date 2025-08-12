package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FoodItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static FoodItem getFoodItemSample1() {
        return new FoodItem().id(1L).name("name1").servingSize("servingSize1").guestsPerUnit(1).maxUnits(1);
    }

    public static FoodItem getFoodItemSample2() {
        return new FoodItem().id(2L).name("name2").servingSize("servingSize2").guestsPerUnit(2).maxUnits(2);
    }

    public static FoodItem getFoodItemRandomSampleGenerator() {
        return new FoodItem()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .servingSize(UUID.randomUUID().toString())
            .guestsPerUnit(intCount.incrementAndGet())
            .maxUnits(intCount.incrementAndGet());
    }
}
