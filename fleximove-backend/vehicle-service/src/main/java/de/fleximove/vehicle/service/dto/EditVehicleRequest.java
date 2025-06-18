package de.fleximove.vehicle.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditVehicleRequest {
    private String identificationNumber;
    private String vehicleModel;
    private String vehicleType;
    private String status;
    private String address;
    private Double priceAmount;
    private String billingModel;
    private Integer minAge;
    private Integer maxBookingTimeMinutes;
    private Double maxDistanceKm;
    private Integer maxPassengers;
    private String requiredLicenseType;
}

