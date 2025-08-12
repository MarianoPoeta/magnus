package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WeeklyPlanTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static WeeklyPlan getWeeklyPlanSample1() {
        return new WeeklyPlan().id(1L).planName("planName1").totalBudgets(1).totalGuests(1);
    }

    public static WeeklyPlan getWeeklyPlanSample2() {
        return new WeeklyPlan().id(2L).planName("planName2").totalBudgets(2).totalGuests(2);
    }

    public static WeeklyPlan getWeeklyPlanRandomSampleGenerator() {
        return new WeeklyPlan()
            .id(longCount.incrementAndGet())
            .planName(UUID.randomUUID().toString())
            .totalBudgets(intCount.incrementAndGet())
            .totalGuests(intCount.incrementAndGet());
    }
}
