package de.fleximove.vehicle.service.dto;

import de.fleximove.vehicle.service.domain.valueobject.VehicleRestrictions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderVehicleResponse {
    private Long vehicleId ;
    private String identNumber;
    private String vehicleModel;
    private String vehicleType;
    private String status;
    private double priceAmount;
    private String billingModel;
    private String address;
    private double latitude;
    private double longitude;
    private double averageVehicleRating;
    private VehicleRestrictions restrictions;
}
