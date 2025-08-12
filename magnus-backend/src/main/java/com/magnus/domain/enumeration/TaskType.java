package com.magnus.domain.enumeration;

/**
 * The TaskType enumeration.
 */
public enum TaskType {
    SHOPPING("Product procurement"),
    RESERVATION("Venue/service booking"),
    DELIVERY("Transportation and delivery"),
    COOKING("Food preparation"),
    PREPARATION("Event setup preparation"),
    SETUP("Event setup"),
    CLEANUP("Post-event cleanup"),
    NEED("Additional requirement");

    private final String value;

    TaskType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
