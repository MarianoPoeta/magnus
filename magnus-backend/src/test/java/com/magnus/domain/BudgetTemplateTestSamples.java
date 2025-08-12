package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BudgetTemplateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BudgetTemplate getBudgetTemplateSample1() {
        return new BudgetTemplate().id(1L).name("name1").category("category1").version(1);
    }

    public static BudgetTemplate getBudgetTemplateSample2() {
        return new BudgetTemplate().id(2L).name("name2").category("category2").version(2);
    }

    public static BudgetTemplate getBudgetTemplateRandomSampleGenerator() {
        return new BudgetTemplate()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString())
            .version(intCount.incrementAndGet());
    }
}
