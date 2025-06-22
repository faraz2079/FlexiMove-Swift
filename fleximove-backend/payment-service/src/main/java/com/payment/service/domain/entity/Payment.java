package com.payment.service.domain.entity;

import com.payment.service.domain.enums.BillingModel;
import com.payment.service.domain.enums.PaymentStatus;
import com.payment.service.domain.valueObject.Amount;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Payment {

    @Id
    @GeneratedValue
    private UUID paymentId;

    private UUID bookingId;
    private Long userId;

    private Amount amount;

    private String currency;

    private String description;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private BillingModel billingModel;

    private LocalDateTime timestamp = LocalDateTime.now();

    private String transactionId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
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

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BillingModel getBillingModel() {
        return billingModel;
    }

    public void setBillingModel(BillingModel billingModel) {
        this.billingModel = billingModel;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void markAsCompleted() {
        if (paymentStatus == PaymentStatus.PENDING)
            this.paymentStatus = PaymentStatus.COMPLETED;
    }

    public void markAsFailed() {
        if (paymentStatus == PaymentStatus.PENDING)
            this.paymentStatus = PaymentStatus.FAILED;
    }

    public void markAsPending() {
        if (paymentStatus == PaymentStatus.PENDING)
            this.paymentStatus = PaymentStatus.PENDING;
    }
}

