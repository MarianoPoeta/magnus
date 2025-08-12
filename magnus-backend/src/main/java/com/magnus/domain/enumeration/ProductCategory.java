package com.magnus.domain.enumeration;

/**
 * The ProductCategory enumeration.
 */
public enum ProductCategory {
    MEAT("Meat products"),
    VEGETABLES("Vegetables and produce"),
    BEVERAGES("Drinks and beverages"),
    CONDIMENTS("Seasonings and condiments"),
    EQUIPMENT("Equipment and tools"),
    DECORATIONS("Decorative items"),
    SUPPLIES("General supplies"),
    OTHER("Other products");

    private final String value;

    ProductCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
