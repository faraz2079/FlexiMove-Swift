package com.instantmobility.booking.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentRequestDTO {
    private long userId;
    private UUID bookingId;
    private BigDecimal amount;
    private String currency = "USD";
    private String paymentMethod;
    private String description;

    // Default constructor
    public PaymentRequestDTO() {}

    // Constructor with essential fields
    public PaymentRequestDTO(long userId, UUID bookingId, BigDecimal amount, String description) {
        this.userId = userId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.description = description;
    }

    // Getters and setters
    public long getUserId() {
        return userId;
    }

    public void setUserId(long l) {
        this.userId = l;
    }

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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}