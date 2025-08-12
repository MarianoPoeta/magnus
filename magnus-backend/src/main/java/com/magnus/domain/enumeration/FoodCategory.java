package com.magnus.domain.enumeration;

/**
 * The FoodCategory enumeration.
 */
public enum FoodCategory {
    APPETIZER("Starter course"),
    MAIN("Main course"),
    DESSERT("Dessert course"),
    BEVERAGE("Drinks"),
    SPECIAL("Special dietary items");

    private final String value;

    FoodCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
