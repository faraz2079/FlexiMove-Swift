package de.fleximove.vehicle.service.domain.valueobject;

public enum VehicleType {
    BICYCLE,
    E_BIKE,
    E_SCOOTER,
    SCOOTER,
    MOTORCYCLE,
    CAR,
    TRUCK;

    public boolean requiresLicense() {
        return switch (this) {
            case MOTORCYCLE, CAR, TRUCK -> true;
            default -> false;
        };
    }

    public boolean requiresMinAge() {
        return switch (this) {
            case MOTORCYCLE, CAR, TRUCK, SCOOTER, E_SCOOTER -> true;
            default -> false;
        };
    }
}
