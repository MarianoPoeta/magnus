package com.magnus.domain.enumeration;

/**
 * The PaymentMethod enumeration.
 */
public enum PaymentMethod {
    CREDIT_CARD("Credit card payment"),
    CASH("Cash payment"),
    BANK_TRANSFER("Bank transfer"),
    CHECK("Check payment"),
    OTHER("Other payment method");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
