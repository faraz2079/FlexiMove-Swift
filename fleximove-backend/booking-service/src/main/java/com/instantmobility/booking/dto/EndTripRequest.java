package com.instantmobility.booking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EndTripRequest {
    private double endLatitude;
    private double endLongitude;
    private LocalDateTime endTime;
}
