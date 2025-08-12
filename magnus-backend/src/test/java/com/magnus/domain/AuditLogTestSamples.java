package com.magnus.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AuditLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AuditLog getAuditLogSample1() {
        return new AuditLog()
            .id(1L)
            .entityType("entityType1")
            .entityId("entityId1")
            .fieldName("fieldName1")
            .userId("userId1")
            .ipAddress("ipAddress1")
            .sessionId("sessionId1");
    }

    public static AuditLog getAuditLogSample2() {
        return new AuditLog()
            .id(2L)
            .entityType("entityType2")
            .entityId("entityId2")
            .fieldName("fieldName2")
            .userId("userId2")
            .ipAddress("ipAddress2")
            .sessionId("sessionId2");
    }

    public static AuditLog getAuditLogRandomSampleGenerator() {
        return new AuditLog()
            .id(longCount.incrementAndGet())
            .entityType(UUID.randomUUID().toString())
            .entityId(UUID.randomUUID().toString())
            .fieldName(UUID.randomUUID().toString())
            .userId(UUID.randomUUID().toString())
            .ipAddress(UUID.randomUUID().toString())
            .sessionId(UUID.randomUUID().toString());
    }
}
