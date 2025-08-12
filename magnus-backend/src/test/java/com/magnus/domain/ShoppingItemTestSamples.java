package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ShoppingItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ShoppingItem getShoppingItemSample1() {
        return new ShoppingItem().id(1L).productName("productName1").supplier("supplier1").supplierContact("supplierContact1").version(1);
    }

    public static ShoppingItem getShoppingItemSample2() {
        return new ShoppingItem().id(2L).productName("productName2").supplier("supplier2").supplierContact("supplierContact2").version(2);
    }

    public static ShoppingItem getShoppingItemRandomSampleGenerator() {
        return new ShoppingItem()
            .id(longCount.incrementAndGet())
            .productName(UUID.randomUUID().toString())
            .supplier(UUID.randomUUID().toString())
            .supplierContact(UUID.randomUUID().toString())
            .version(intCount.incrementAndGet());
    }
}
