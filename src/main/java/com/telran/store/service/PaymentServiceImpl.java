package com.telran.store.service;

import com.telran.store.entity.Order;
import com.telran.store.entity.Payment;
import com.telran.store.enums.Status;
import com.telran.store.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;

    @Override
    public void pay(BigDecimal paymentAmount, Long orderId) {
        Order order = orderService.getById(orderId);
        BigDecimal totalPrice = order.getTotalPrice();

        if (order.getStatus() == Status.PAID){
            throw new RuntimeException("Order has already been paid");
        }

        BigDecimal alreadyPaid = order.getPayments().stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add).add(paymentAmount);

        if (alreadyPaid.compareTo(totalPrice) > 0) {
            throw new RuntimeException("The amount of payment exceeds the total amount of the order");
        }

        Payment payment = new Payment();
        payment.setUser(order.getShopUser());
        payment.setOrder(order);
        payment.setAmount(paymentAmount);
        payment.setDateTime(LocalDateTime.now());

        if (alreadyPaid.compareTo(totalPrice) == 0){
            order.setStatus(Status.PAID);
        } else {
            order.setStatus(Status.PARTIALLY_PAID);
        }
        paymentRepository.save(payment);
    }
}
