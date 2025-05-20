package com.payment.service.controller;

import com.payment.service.domain.entity.Payment;
import com.payment.service.service.PaymentProcessingService;
import com.payment.service.domain.repo.PaymentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
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

    @GetMapping("/{id}")
    public Payment get(@PathVariable UUID id) {
        return repository.findById(id).orElseThrow();
    }

    @PostMapping("/{id}/process")
    public String process(@PathVariable UUID id) {
        processingService.processPayment(id);
        return "Processed.";
    }
}
