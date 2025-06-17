package com.payment.service.service.DTO;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentResponseDTO {
    private UUID paymentId;
    private String paymentStatus;
    private String message;

    private UUID bookingId;
    private BigDecimal amount;
    private String currency;
    private String description;

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PaymentResponseDTO(UUID paymentId, String paymentStatus, String message,
                              UUID bookingId, BigDecimal amount, String currency, String description) {
        this.paymentId = paymentId;
        this.paymentStatus = paymentStatus;
        this.message = message;
        this.bookingId = bookingId;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
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
