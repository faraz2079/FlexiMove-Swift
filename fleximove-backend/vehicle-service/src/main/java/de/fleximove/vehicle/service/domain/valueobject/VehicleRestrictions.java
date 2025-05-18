package de.fleximove.vehicle.service.domain.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class VehicleRestrictions {
    private Integer minAge;
    private Integer maxBookingTimeMinutes;
    private Double maxDistanceKm;
    private Integer maxPassengers;
    @Enumerated(EnumType.STRING)
    private DriverLicenseType requiredLicense;

    public VehicleRestrictions(Integer minAge, Integer maxBookingTimeMinutes, Double maxDistanceKm, Integer maxPassengers, DriverLicenseType driverLicenseType){
        this.minAge = minAge;
        this.maxBookingTimeMinutes = maxBookingTimeMinutes;
        this.maxDistanceKm = maxDistanceKm;
        this.maxPassengers = maxPassengers;
        this.requiredLicense = driverLicenseType;
    }

    public VehicleRestrictions() {

    }
}

