package de.fhdo.spring.user.context.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BookingDto {
    private UUID id;
    private Long userId;
    private Long vehicleId;
    private String startDate;
    private String endDate;
}
