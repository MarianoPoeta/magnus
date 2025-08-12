package com.magnus.domain.enumeration;

/**
 * The AuditAction enumeration.
 */
public enum AuditAction {
    CREATE("Entity created"),
    UPDATE("Entity updated"),
    DELETE("Entity deleted"),
    STATUS_CHANGE("Status changed"),
    APPROVE("Entity approved"),
    REJECT("Entity rejected");

    private final String value;

    AuditAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
