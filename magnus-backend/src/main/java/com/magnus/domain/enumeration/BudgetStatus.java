package com.magnus.domain.enumeration;

/**
 * The BudgetStatus enumeration.
 */
public enum BudgetStatus {
    DRAFT("Initial creation"),
    PENDING("Waiting for approval"),
    APPROVED("Approved but not reserved"),
    RESERVA("Reserved - triggers workflow"),
    REJECTED("Rejected proposal"),
    COMPLETED("Event completed"),
    CANCELED("Canceled event");

    private final String value;

    BudgetStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
