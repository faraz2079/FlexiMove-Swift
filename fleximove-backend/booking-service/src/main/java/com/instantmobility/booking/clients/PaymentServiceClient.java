package com.instantmobility.booking.clients;

import com.instantmobility.booking.dto.PaymentRequestDTO;
import com.instantmobility.booking.dto.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@FeignClient(name = "payment-service")
public interface PaymentServiceClient {
    @PostMapping("${paymentService.paymentProcess.endpoint}")
    PaymentResponseDTO processPayment(@RequestBody PaymentRequestDTO request);

    @GetMapping("${paymentService.paymentStatus.endpoint}")
    PaymentResponseDTO getPaymentStatus(@PathVariable UUID paymentId);

}
