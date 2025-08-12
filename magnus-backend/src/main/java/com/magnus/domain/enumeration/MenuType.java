package com.magnus.domain.enumeration;

/**
 * The MenuType enumeration.
 */
public enum MenuType {
    BREAKFAST("Morning meal"),
    LUNCH("Midday meal"),
    DINNER("Evening meal"),
    BRUNCH("Late morning meal"),
    COCKTAIL("Cocktail service"),
    CATERING("Catering service"),
    SNACKS("Light refreshments");

    private final String value;

    MenuType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
