package com.instantmobility.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateBookingRequest {
    private Long userId;
    private Long vehicleId;
    private LocalDateTime startTime;
    private double pickupLatitude;
    private double pickupLongitude;
}
