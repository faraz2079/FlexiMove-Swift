package com.payment.service.service;
import com.payment.service.domain.entity.Payment;
import org.springframework.stereotype.Component;


// Kafka Mock
// for real kafka implentation we should implement the kafka template
@Component
public class PaymentEventPublisher {
    public void publishPaymentProcessed(Payment payment) {
        System.out.println("Event published for payment: " + payment.getPaymentId());
    }

}
