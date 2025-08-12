package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Client getClientSample1() {
        return new Client().id(1L).name("name1").email("email1").phone("phone1").company("company1").taxId("taxId1");
    }

    public static Client getClientSample2() {
        return new Client().id(2L).name("name2").email("email2").phone("phone2").company("company2").taxId("taxId2");
    }

    public static Client getClientRandomSampleGenerator() {
        return new Client()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .company(UUID.randomUUID().toString())
            .taxId(UUID.randomUUID().toString());
    }
}
