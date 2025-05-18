package de.fleximove.vehicle.service.dto;

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

    public VehicleRequest() {}

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public double getPriceAmount() {
        return priceAmount;
    }

    public String getBillingModel() {
        return billingModel;
    }

    public String getAddress() {
        return address;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public Integer getMaxBookingTimeMinutes() {
        return maxBookingTimeMinutes;
    }

    public Double getMaxDistanceKm() {
        return maxDistanceKm;
    }

    public Integer getMaxPassengers() {
        return maxPassengers;
    }

    public String getRequiredLicenseType() {
        return requiredLicenseType;
    }
}

