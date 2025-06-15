package de.fleximove.vehicle.service.domain.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
public class VehicleRestrictions {
    private Integer minAge;
    private Integer maxBookingTimeMinutes;
    private Double maxDistanceKm;
    private Integer maxPassengers;
    @Enumerated(EnumType.STRING)
    private DriverLicenseType requiredLicense;

    public VehicleRestrictions(Integer minAge, Integer maxBookingTimeMinutes, Double maxDistanceKm, Integer maxPassengers, DriverLicenseType driverLicenseType){
        this.minAge = minAge;
        this.maxBookingTimeMinutes = Objects.requireNonNull(maxBookingTimeMinutes, "maxBookingTimeMinutes must not be null");
        this.maxDistanceKm = Objects.requireNonNull(maxDistanceKm, "maxDistanceKm must not be null");
        this.maxPassengers = Objects.requireNonNull(maxPassengers, "maxPassengers must not be null");
        this.requiredLicense = Objects.requireNonNull(driverLicenseType, "driverLicenseType must not be null");
    }
}

