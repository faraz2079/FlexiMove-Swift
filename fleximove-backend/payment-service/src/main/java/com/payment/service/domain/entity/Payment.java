package com.payment.service.domain.entity;

import com.payment.service.domain.enums.BillingModel;
import com.payment.service.domain.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Payment {
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

    @Id
    @GeneratedValue
    private UUID paymentId;

    private UUID bookingId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private BillingModel billingModel;

    private LocalDateTime timestamp = LocalDateTime.now();

    public void markAsCompleted() {
        if (paymentStatus == PaymentStatus.PENDING)
            this.paymentStatus = PaymentStatus.COMPLETED;
    }

    public void markAsFailed() {
        if (paymentStatus == PaymentStatus.PENDING)
            this.paymentStatus = PaymentStatus.FAILED;
    }


}
