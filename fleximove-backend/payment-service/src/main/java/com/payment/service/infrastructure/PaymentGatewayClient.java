package com.payment.service.infrastructure;

import com.payment.service.domain.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayClient {
    public boolean charge(Payment payment) {
        // Simulate external payment gateway call
        return true; // simulate success
    }
}
