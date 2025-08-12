package com.magnus.domain.enumeration;

/**
 * The DependencyType enumeration.
 */
public enum DependencyType {
    BLOCKS("Task blocks another"),
    REQUIRES("Task requires another"),
    SUGGESTS("Task suggests another");

    private final String value;

    DependencyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
