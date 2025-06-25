package com.instantmobility.booking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PaymentResponse {
    private UUID paymentId;
    private String paymentStatus;
    private String message;
    private double amount;
    private LocalDateTime timeStamp;
    private String transactionId;
}