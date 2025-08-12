package com.magnus.domain.enumeration;

/**
 * The TaskStatus enumeration.
 */
public enum TaskStatus {
    TODO("Not started"),
    IN_PROGRESS("Currently being worked on"),
    DONE("Completed successfully"),
    BLOCKED("Blocked by dependencies"),
    CANCELED("Canceled task");

    private final String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
