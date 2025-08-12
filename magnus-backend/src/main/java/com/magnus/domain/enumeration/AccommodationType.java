package com.magnus.domain.enumeration;

/**
 * The AccommodationType enumeration.
 */
public enum AccommodationType {
    SINGLE("Single room"),
    DOUBLE("Double room"),
    SUITE("Suite room"),
    APARTMENT("Apartment rental"),
    VILLA("Villa rental"),
    HOSTEL("Hostel accommodation");

    private final String value;

    AccommodationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
