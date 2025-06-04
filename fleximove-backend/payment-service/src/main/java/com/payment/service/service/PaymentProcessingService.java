package com.payment.service.service;
import com.instantmobility.booking.service.BookingService;
import com.payment.service.infrastructure.PaymentGatewayClient;
import com.payment.service.domain.entity.Payment;
import com.payment.service.domain.repo.PaymentRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PaymentProcessingService {

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayClient gatewayClient;
    private final PaymentEventPublisher eventPublisher;
//    private final BookingService bookingService;


    public PaymentProcessingService(PaymentRepository paymentRepository,
                                    PaymentGatewayClient gatewayClient,
                                    PaymentEventPublisher eventPublisher ) {
        this.paymentRepository = paymentRepository;
        this.gatewayClient = gatewayClient;
        this.eventPublisher = eventPublisher;
//        this.bookingService = bookingService;
    }

    public void processPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        try {
            boolean success = gatewayClient.charge(payment);

            if (success) {
                payment.markAsCompleted();
            } else {
                payment.markAsFailed();
            }

        } catch (Exception e) {
            payment.markAsFailed();
        }

        paymentRepository.save(payment);
        eventPublisher.publishPaymentProcessed(payment);
    }
}
