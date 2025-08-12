package com.magnus.domain.enumeration;

/**
 * The MealType enumeration.
 */
public enum MealType {
    BREAKFAST("Morning meal"),
    LUNCH("Midday meal"),
    DINNER("Evening meal"),
    SNACK("Light meal");

    private final String value;

    MealType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
