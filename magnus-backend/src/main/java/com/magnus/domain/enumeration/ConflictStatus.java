package com.magnus.domain.enumeration;

/**
 * The ConflictStatus enumeration.
 */
public enum ConflictStatus {
    NONE("No conflicts"),
    DETECTED("Conflict detected"),
    RESOLVED("Conflict resolved"),
    ESCALATED("Conflict escalated");

    private final String value;

    ConflictStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
