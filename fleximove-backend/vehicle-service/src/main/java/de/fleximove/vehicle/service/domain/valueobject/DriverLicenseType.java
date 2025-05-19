package de.fleximove.vehicle.service.domain.valueobject;

import java.util.Arrays;

public enum DriverLicenseType {
    MOTORCYCLE("A"),
    CAR("B"),
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

    public static DriverLicenseType licenseTypeFromCode(String code) {
        return Arrays.stream(DriverLicenseType.values())
                .filter(value -> value.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid license: " + code));
    }

}

