package com.telran.store.repository;

import com.telran.store.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    List<Payment> findAllByOrderId(Long orderId);
}
