package com.payment.service.service.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentResponseDTO {
    private UUID paymentId;
    private String paymentStatus;
    private String message;
    private double amount;
    private LocalDateTime timeStamp;
    private String transactionId;

    public PaymentResponseDTO(UUID paymentId, String paymentStatus, String message,
                              double amount, LocalDateTime timeStamp, String transactionId) {
        this.paymentId = paymentId;
        this.paymentStatus = paymentStatus;
        this.message = message;
        this.amount = amount;
        this.timeStamp = timeStamp;
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getMessage() {
        return message;
    }
}
