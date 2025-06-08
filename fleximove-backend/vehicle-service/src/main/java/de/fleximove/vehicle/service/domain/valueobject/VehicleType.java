package de.fleximove.vehicle.service.domain.valueobject;

public enum VehicleType {
    BICYCLE,
    E_BIKE,
    E_SCOOTER,
    SCOOTER,
    MOTORCYCLE,
    CAR,
    BUS,
    TRUCK;

    public boolean requiresLicense() {
        return switch (this) {
            case MOTORCYCLE, CAR, TRUCK, BUS -> true;
            default -> false;
        };
    }

    public boolean requiresMinAge() {
        return switch (this) {
            case MOTORCYCLE, CAR, TRUCK, SCOOTER, E_SCOOTER, BUS -> true;
            default -> false;
        };
    }
}
