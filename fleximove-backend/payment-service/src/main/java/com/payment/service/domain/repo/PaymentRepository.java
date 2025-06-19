package com.payment.service.domain.repo;

import com.payment.service.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    void deletePaymentInfoByUserId(Long userId);
    List<Payment> findByUserId(Long userId);
    List<Payment> findByBookingId(UUID bookingId);
}
