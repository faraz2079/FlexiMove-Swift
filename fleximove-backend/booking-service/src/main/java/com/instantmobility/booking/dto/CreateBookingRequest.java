package com.instantmobility.booking.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@NoArgsConstructor
public class CreateBookingRequest {
    private Long userId;
    private Long vehicleId;
    private LocalDateTime startTime;
    private double pickupLatitude;
    private double pickupLongitude;
    // User validation fields
    private int userAge;
    private String userLicenseType;

    // Vehicle requirement fields
    private int vehicleMinimumAge;
    private String vehicleRequiredLicenses;

    public int getUserAge() {
        return userAge;
    }

    public String getUserLicenseType() {
        return userLicenseType;
    }

    // Getters for vehicle requirement fields
    public int getVehicleMinimumAge() {
        return vehicleMinimumAge;
    }

    public String getVehicleRequiredLicense() {
        return vehicleRequiredLicenses;
    }

    // Setters for user validation fields
    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public void setUserLicenseType(String userLicenseType) {
        this.userLicenseType = userLicenseType;
    }

    // Setters for vehicle requirement fields
    public void setVehicleMinimumAge(int vehicleMinimumAge) {
        this.vehicleMinimumAge = vehicleMinimumAge;
    }

    public void setVehicleRequiredLicense(String vehicleRequiredLicenses) {
        this.vehicleRequiredLicenses = vehicleRequiredLicenses;
    }

}
