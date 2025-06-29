package com.telran.store.service;

import com.telran.store.entity.Order;
import com.telran.store.entity.Payment;
import com.telran.store.enums.Status;
import com.telran.store.repository.OrderRepository;
import com.telran.store.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void pay(BigDecimal totalPrice, Long orderId) {
        Order order = orderRepository.getById(orderId);

        if (order.getStatus() == Status.PAID){
            throw new RuntimeException("Order has already been paid");
        }

        BigDecimal newPayment = totalPrice;
        BigDecimal totalAmount = order.;

        Map<Long, BigDecimal> payments = new HashMap<>();
        for (Payment payment : order.getPayments()) {
            payments.put(payment.getId(), payment.getAmount());
        }

        BigDecimal alreadyPaid = BigDecimal.ZERO;
        for (BigDecimal amount : payments.values()) {
            alreadyPaid = alreadyPaid.add(amount);
        }

        BigDecimal paidAmount = alreadyPaid.add(newPayment);

        if (paidAmount.compareTo(totalAmount) > 0) {
            throw new RuntimeException("The payment amount exceeds the order amount");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(totalPrice);
        payment.setDateTime(LocalDateTime.now());
        payment.setUser(order.getShopUser());
        paymentRepository.save(payment);

        if (paidAmount.compareTo(totalAmount) == 0){
            order.setStatus(Status.PAID);
        } else {
            order.setStatus(Status.PARTIALLY_PAID);
        }

        orderRepository.save(order);
    }
}
