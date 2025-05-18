package de.fleximove.vehicle.service.domain.valueobject;

public enum DriverLicenseType {
    MOTORCYCLE("A"),
    CAR("B"),
    VAN("B"),
    TRUCK("C"),
    BUS("D"),
    NONE("NONE");

    private final String code;

    DriverLicenseType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

