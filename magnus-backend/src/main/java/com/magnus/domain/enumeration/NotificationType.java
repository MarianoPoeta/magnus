package com.magnus.domain.enumeration;

/**
 * The NotificationType enumeration.
 */
public enum NotificationType {
    INFO("Information"),
    SUCCESS("Success message"),
    WARNING("Warning message"),
    ERROR("Error message");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
