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
<<<<<<< HEAD
    private double amount;
    private LocalDateTime timeStamp;
    private String transactionId;
=======

    // Getters and setters
    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }


    // Helper method to check if payment was successful
    public boolean isSuccessful() {
        return "COMPLETED".equals(status);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String s) {
    }
>>>>>>> 4776a39 (add latest changes)
}