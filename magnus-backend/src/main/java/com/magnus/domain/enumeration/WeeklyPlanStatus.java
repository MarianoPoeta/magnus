package com.magnus.domain.enumeration;

/**
 * The WeeklyPlanStatus enumeration.
 */
public enum WeeklyPlanStatus {
    DRAFT("Planning in progress"),
    IN_PROGRESS("Plan being executed"),
    COMPLETED("Plan completed"),
    ARCHIVED("Plan archived");

    private final String value;

    WeeklyPlanStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
