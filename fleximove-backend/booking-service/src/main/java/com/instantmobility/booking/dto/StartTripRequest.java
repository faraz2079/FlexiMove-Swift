package com.instantmobility.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class StartTripRequest {
    private double startLatitude;
    private double startLongitude;
    private LocalDateTime startTime;
}