package com.payment.service.service;

import com.payment.service.domain.entity.Payment;
import com.payment.service.domain.enums.PaymentStatus;
import com.payment.service.domain.repo.PaymentRepository;
import com.payment.service.domain.valueObject.Amount;
import com.payment.service.infrastructure.PaymentGatewayClient;
import com.payment.service.service.DTO.PaymentRequestDTO;
import com.payment.service.service.DTO.PaymentResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentProcessingService {

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayClient gatewayClient;
    private final PaymentEventPublisher eventPublisher;

    public PaymentProcessingService(PaymentRepository paymentRepository,
                                    PaymentGatewayClient gatewayClient,
                                    PaymentEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.gatewayClient = gatewayClient;
        this.eventPublisher = eventPublisher;
    }

    public PaymentResponseDTO processPayment(PaymentRequestDTO dto) {
        Payment payment = new Payment();
        payment.setBookingId(dto.getBookingId());
        payment.setUserId(dto.getUserId());
        payment.setAmount(new Amount(dto.getAmount()));
        payment.setCurrency(dto.getCurrency());
        payment.setDescription(dto.getDescription());
        payment.setTimestamp(LocalDateTime.now());
        payment.setTransactionId(UUID.randomUUID().toString());

        // Simulate logic (no real gateway used)
        if (dto.getAmount() == 0) {
            // Maybe the user hasn't confirmed yet or system delay
            payment.markAsPending();
        } else if (dto.getAmount() > 0) {
            payment.markAsCompleted();
        } else {
            payment.markAsFailed();
        }

        Payment saved = paymentRepository.save(payment);
        eventPublisher.publishPaymentProcessed(saved);

        return new PaymentResponseDTO(
                saved.getPaymentId(),
                saved.getPaymentStatus().toString(),
                saved.getPaymentStatus() == PaymentStatus.COMPLETED ? "Payment successful" : "Payment failed",
                saved.getAmount().getAmountValue(),
                saved.getTimestamp(),
                saved.getTransactionId()
        );
    }

    public PaymentResponseDTO getPaymentStatus(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("No payment found with ID: " + paymentId));

        return new PaymentResponseDTO(
                payment.getPaymentId(),
                payment.getPaymentStatus().toString(),
                "Payment status retrieved",
                payment.getAmount().getAmountValue(),
                payment.getTimestamp(),
                payment.getTransactionId()
        );
    }

    public void deletePaymentsByUserId(Long userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);
        if (payments.isEmpty()) {
            throw new EntityNotFoundException("No payments found for user ID: " + userId);
        }
        paymentRepository.deleteAll(payments);
    }

    public void deletePaymentsByBookingId(UUID bookingId) {
        List<Payment> payments = paymentRepository.findByBookingId(bookingId);
        if (payments.isEmpty()) {
            throw new EntityNotFoundException("No payments found for booking: " + bookingId);
        }
        paymentRepository.deleteAll(payments);
    }
}