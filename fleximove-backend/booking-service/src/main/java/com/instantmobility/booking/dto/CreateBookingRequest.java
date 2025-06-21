package com.instantmobility.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
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
    private String vehicleRequiredLicense;
}
