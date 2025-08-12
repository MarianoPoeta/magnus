package com.magnus.domain.enumeration;

/**
 * The EventGender enumeration.
 */
public enum EventGender {
    MEN("Men&#39;s event"),
    WOMEN("Women&#39;s event"),
    MIXED("Mixed gender event");

    private final String value;

    EventGender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
