package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Payment getPaymentSample1() {
        return new Payment().id(1L).reference("reference1").confirmationNumber("confirmationNumber1");
    }

    public static Payment getPaymentSample2() {
        return new Payment().id(2L).reference("reference2").confirmationNumber("confirmationNumber2");
    }

    public static Payment getPaymentRandomSampleGenerator() {
        return new Payment()
            .id(longCount.incrementAndGet())
            .reference(UUID.randomUUID().toString())
            .confirmationNumber(UUID.randomUUID().toString());
    }
}
