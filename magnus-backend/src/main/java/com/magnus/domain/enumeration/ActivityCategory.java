package com.magnus.domain.enumeration;

/**
 * The ActivityCategory enumeration.
 */
public enum ActivityCategory {
    ADVENTURE("Adventure activities"),
    OUTDOOR("Outdoor activities"),
    NIGHTLIFE("Nightlife entertainment"),
    DINING("Dining experiences"),
    INDOOR("Indoor activities"),
    CULTURAL("Cultural experiences"),
    SPORTS("Sports activities"),
    ENTERTAINMENT("Entertainment shows");

    private final String value;

    ActivityCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
