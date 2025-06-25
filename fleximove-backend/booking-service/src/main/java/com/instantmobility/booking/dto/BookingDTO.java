package com.instantmobility.booking.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Data
public class BookingDTO {
    private UUID id;
    private Long userId;
    private Long vehicleId;
    private String status;
    private LocalDateTime bookedAt;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long durationInMinutes;
    private double pickupLatitude;
    private double pickupLongitude;
    private double dropoffLatitude;
    private double dropoffLongitude;
    private double cost;
    private double distance;
}