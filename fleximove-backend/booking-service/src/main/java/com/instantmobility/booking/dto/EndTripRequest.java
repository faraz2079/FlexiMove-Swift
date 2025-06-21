package com.instantmobility.booking.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@NoArgsConstructor
public class EndTripRequest {
    private double endLatitude;
    private double endLongitude;
    private LocalDateTime endTime;
}
