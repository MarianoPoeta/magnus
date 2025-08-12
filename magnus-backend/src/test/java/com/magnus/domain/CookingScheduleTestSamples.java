package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CookingScheduleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CookingSchedule getCookingScheduleSample1() {
        return new CookingSchedule()
            .id(1L)
            .menuName("menuName1")
            .guestCount(1)
            .estimatedDuration(1)
            .actualDuration(1)
            .version(1)
            .lastModifiedBy("lastModifiedBy1");
    }

    public static CookingSchedule getCookingScheduleSample2() {
        return new CookingSchedule()
            .id(2L)
            .menuName("menuName2")
            .guestCount(2)
            .estimatedDuration(2)
            .actualDuration(2)
            .version(2)
            .lastModifiedBy("lastModifiedBy2");
    }

    public static CookingSchedule getCookingScheduleRandomSampleGenerator() {
        return new CookingSchedule()
            .id(longCount.incrementAndGet())
            .menuName(UUID.randomUUID().toString())
            .guestCount(intCount.incrementAndGet())
            .estimatedDuration(intCount.incrementAndGet())
            .actualDuration(intCount.incrementAndGet())
            .version(intCount.incrementAndGet())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
