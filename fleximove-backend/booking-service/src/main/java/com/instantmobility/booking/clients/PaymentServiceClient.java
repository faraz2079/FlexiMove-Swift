package com.instantmobility.booking.clients;

import com.instantmobility.booking.clients.fallback.PaymentServiceFallbackFactory;
import com.instantmobility.booking.dto.PaymentRequest;
import com.instantmobility.booking.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@FeignClient(
        name = "payment-service",
        fallbackFactory = PaymentServiceFallbackFactory.class
)
public interface PaymentServiceClient {
    @PostMapping("${paymentService.paymentProcess.endpoint}")
    PaymentResponse processPayment(@RequestBody PaymentRequest request);

    @GetMapping("${paymentService.paymentStatus.endpoint}")
    PaymentResponse getPaymentStatus(@PathVariable UUID paymentId);

    @DeleteMapping("${paymentService.deleteAllPaymentInfoForUser.endpoint}")
    void deletePaymentsByUser(@PathVariable Long userId);

    @DeleteMapping("${paymentService.deleteAllPaymentInfoForBooking.endpoint}")
    void deletePaymentsByBookingId(@PathVariable UUID bookingId);
}
