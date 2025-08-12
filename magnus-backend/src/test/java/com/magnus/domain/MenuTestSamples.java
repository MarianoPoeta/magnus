package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MenuTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Menu getMenuSample1() {
        return new Menu().id(1L).name("name1").minPeople(1).maxPeople(1).restaurant("restaurant1").preparationTime(1).version(1);
    }

    public static Menu getMenuSample2() {
        return new Menu().id(2L).name("name2").minPeople(2).maxPeople(2).restaurant("restaurant2").preparationTime(2).version(2);
    }

    public static Menu getMenuRandomSampleGenerator() {
        return new Menu()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .minPeople(intCount.incrementAndGet())
            .maxPeople(intCount.incrementAndGet())
            .restaurant(UUID.randomUUID().toString())
            .preparationTime(intCount.incrementAndGet())
            .version(intCount.incrementAndGet());
    }
}
