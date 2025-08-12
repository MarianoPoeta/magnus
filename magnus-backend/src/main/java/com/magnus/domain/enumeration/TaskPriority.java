package com.magnus.domain.enumeration;

/**
 * The TaskPriority enumeration.
 */
public enum TaskPriority {
    LOW("Low priority"),
    MEDIUM("Medium priority"),
    HIGH("High priority"),
    URGENT("Urgent - immediate attention");

    private final String value;

    TaskPriority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
