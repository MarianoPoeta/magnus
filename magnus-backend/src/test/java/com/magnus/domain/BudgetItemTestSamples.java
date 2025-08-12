package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BudgetItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BudgetItem getBudgetItemSample1() {
        return new BudgetItem().id(1L).itemType("itemType1").templateId("templateId1").templateName("templateName1").quantity(1).version(1);
    }

    public static BudgetItem getBudgetItemSample2() {
        return new BudgetItem().id(2L).itemType("itemType2").templateId("templateId2").templateName("templateName2").quantity(2).version(2);
    }

    public static BudgetItem getBudgetItemRandomSampleGenerator() {
        return new BudgetItem()
            .id(longCount.incrementAndGet())
            .itemType(UUID.randomUUID().toString())
            .templateId(UUID.randomUUID().toString())
            .templateName(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet())
            .version(intCount.incrementAndGet());
    }
}
