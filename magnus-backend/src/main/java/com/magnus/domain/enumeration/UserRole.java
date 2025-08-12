package com.magnus.domain.enumeration;

/**
 * The UserRole enumeration.
 */
public enum UserRole {
    ADMIN("Full system access"),
    SALES("Budget creation and client management"),
    LOGISTICS("Task management and shopping coordination"),
    COOK("Cooking schedules and ingredient management");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
