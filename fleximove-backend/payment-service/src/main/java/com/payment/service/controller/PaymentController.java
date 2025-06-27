package com.payment.service.controller;

import com.payment.service.domain.entity.Payment;
import com.payment.service.domain.repo.PaymentRepository;
import com.payment.service.service.DTO.PaymentResponseDTO;
import com.payment.service.service.PaymentProcessingService;
import com.payment.service.service.DTO.PaymentRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentRepository repository;
    private final PaymentProcessingService processingService;

    public PaymentController(PaymentRepository repo, PaymentProcessingService service) {
        this.repository = repo;
        this.processingService = service;
    }

    @PostMapping
    public Payment create(@RequestBody Payment payment) {
        return repository.save(payment);
    }

    @GetMapping("/{paymentId}")
    public Payment get(@PathVariable UUID paymentId) {
        return repository.findById(paymentId).orElseThrow();
    }

    @PostMapping("/process")
    public PaymentResponseDTO processFromBooking(@RequestBody PaymentRequestDTO request) {
        return processingService.processPayment(request);
    }

    @GetMapping("/status/{paymentId}")
    public ResponseEntity<PaymentResponseDTO> getStatus(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(processingService.getPaymentStatus(paymentId));
    }

    @DeleteMapping("/deleteByUser/{userId}")
    public ResponseEntity<?> deletePaymentsByUserId(@PathVariable Long userId) {
        try {
            processingService.deletePaymentsByUserId(userId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error while deleting payments: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteAll/forBooking/{bookingId}")
    public ResponseEntity<?> deletePaymentsByBookingId(@PathVariable UUID bookingId) {
        try {
            processingService.deletePaymentsByBookingId(bookingId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error while deleting payments: " + e.getMessage());
        }
    }
}