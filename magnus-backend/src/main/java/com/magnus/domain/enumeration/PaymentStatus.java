package com.magnus.domain.enumeration;

/**
 * The PaymentStatus enumeration.
 */
public enum PaymentStatus {
    UNPAID("No payment received"),
    PARTIALLY_PAID("Partial payment received"),
    PAID("Fully paid");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
