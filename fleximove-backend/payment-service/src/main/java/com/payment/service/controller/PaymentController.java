package com.payment.service.controller;

import com.payment.service.domain.entity.Payment;
import com.payment.service.domain.repo.PaymentRepository;
import com.payment.service.service.PaymentProcessingService;
import com.payment.service.service.DTO.PaymentRequestDTO;
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
    public Payment get(@PathVariable("paymentId") UUID paymentId) {
        return repository.findById(paymentId).orElseThrow();
    }

    // Main endpoint for processing payments from Booking
    @PostMapping("/process")
    public String processFromBooking(@RequestBody PaymentRequestDTO request) {
        processingService.processPayment(request);
        return "Payment processed.";
    }
}
