package com.magnus.domain.enumeration;

/**
 * The VehicleType enumeration.
 */
public enum VehicleType {
    BUS("Bus transportation"),
    MINIVAN("Minivan"),
    CAR("Car rental"),
    LIMOUSINE("Limousine service"),
    BOAT("Boat charter"),
    MOTORCYCLE("Motorcycle rental");

    private final String value;

    VehicleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
