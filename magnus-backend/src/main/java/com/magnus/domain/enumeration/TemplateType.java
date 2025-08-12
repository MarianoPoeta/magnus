package com.magnus.domain.enumeration;

/**
 * The TemplateType enumeration.
 */
public enum TemplateType {
    MENU("Menu template"),
    ACTIVITY("Activity template"),
    TRANSPORT("Transport template"),
    ACCOMMODATION("Accommodation template"),
    BUDGET("Budget template");

    private final String value;

    TemplateType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
