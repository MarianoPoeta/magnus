package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SystemConfigTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SystemConfig getSystemConfigSample1() {
        return new SystemConfig().id(1L).configKey("configKey1").description("description1").category("category1").dataType("dataType1");
    }

    public static SystemConfig getSystemConfigSample2() {
        return new SystemConfig().id(2L).configKey("configKey2").description("description2").category("category2").dataType("dataType2");
    }

    public static SystemConfig getSystemConfigRandomSampleGenerator() {
        return new SystemConfig()
            .id(longCount.incrementAndGet())
            .configKey(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString())
            .dataType(UUID.randomUUID().toString());
    }
}
