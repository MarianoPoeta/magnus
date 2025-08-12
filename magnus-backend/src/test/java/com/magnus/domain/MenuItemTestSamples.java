package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MenuItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MenuItem getMenuItemSample1() {
        return new MenuItem().id(1L).name("name1").preparationTime(1);
    }

    public static MenuItem getMenuItemSample2() {
        return new MenuItem().id(2L).name("name2").preparationTime(2);
    }

    public static MenuItem getMenuItemRandomSampleGenerator() {
        return new MenuItem()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .preparationTime(intCount.incrementAndGet());
    }
}
