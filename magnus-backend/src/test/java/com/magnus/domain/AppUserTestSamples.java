package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppUser getAppUserSample1() {
        return new AppUser()
            .id(1L)
            .login("login1")
            .firstName("firstName1")
            .lastName("lastName1")
            .email("email1")
            .phone("phone1")
            .profilePicture("profilePicture1");
    }

    public static AppUser getAppUserSample2() {
        return new AppUser()
            .id(2L)
            .login("login2")
            .firstName("firstName2")
            .lastName("lastName2")
            .email("email2")
            .phone("phone2")
            .profilePicture("profilePicture2");
    }

    public static AppUser getAppUserRandomSampleGenerator() {
        return new AppUser()
            .id(longCount.incrementAndGet())
            .login(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .profilePicture(UUID.randomUUID().toString());
    }
}
