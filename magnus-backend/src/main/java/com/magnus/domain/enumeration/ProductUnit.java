package com.magnus.domain.enumeration;

/**
 * The ProductUnit enumeration.
 */
public enum ProductUnit {
    KG("Kilograms"),
    G("Grams"),
    LITERS("Liters"),
    ML("Milliliters"),
    UNITS("Individual units"),
    PIECES("Pieces"),
    BOXES("Boxes"),
    BAGS("Bags"),
    BOTTLES("Bottles"),
    BUNCHES("Bunches"),
    LEAVES("Leaves"),
    SLICES("Slices");

    private final String value;

    ProductUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
