package de.fleximove.vehicle.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VehicleRequest {

    private String identificationNumber;
    private String vehicleType;
    private double priceAmount;
    private String billingModel;
    private String address;
    private Integer minAge;
    private Integer maxBookingTimeMinutes;
    private Double maxDistanceKm;
    private Integer maxPassengers;
    private String requiredLicenseType;
}

