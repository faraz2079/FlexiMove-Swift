package com.instantmobility.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PaymentRequest {
    private long userId;
    private UUID bookingId;
    private double amount;
    private String currency = "USD";
    private String description;

    public PaymentRequest(long userId, UUID bookingId, double amount, String description) {
        this.userId = userId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.description = description;
    }
}