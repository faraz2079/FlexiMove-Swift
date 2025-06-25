package com.instantmobility.booking.clients.fallback;

import com.instantmobility.booking.clients.PaymentServiceClient;
import com.instantmobility.booking.dto.PaymentResponse;
import com.instantmobility.booking.dto.PaymentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentServiceFallbackFactory implements FallbackFactory<PaymentServiceClient> {
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceFallbackFactory.class);

    @Override
    public PaymentServiceClient create(Throwable cause) {
        logger.error("Payment service fallback triggered due to: {}", cause.getMessage());

        return new PaymentServiceClient() {
            @Override
            public PaymentResponse processPayment(PaymentRequest request) {
                logger.warn("Fallback: Cannot process payment for booking {}", request.getBookingId());
                PaymentResponse response = new PaymentResponse();
                response.setPaymentStatus("FAILED");
                response.setMessage("Payment service unavailable. Please try again later.");
                return response;
            }

            @Override
            public PaymentResponse getPaymentStatus(UUID paymentId) {
                return null;
            }

            @Override
            public void deletePaymentsByUser(Long userId) {

            }

            @Override
            public void deletePaymentsByBookingId(UUID bookingId) {

            }
        };
    }
}