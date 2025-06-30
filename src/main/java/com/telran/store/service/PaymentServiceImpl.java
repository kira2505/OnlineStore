package com.telran.store.service;

import com.telran.store.dto.PaymentCreateDto;
import com.telran.store.dto.PaymentResponseDto;
import com.telran.store.entity.Order;
import com.telran.store.entity.Payment;
import com.telran.store.enums.PaymentStatus;
import com.telran.store.mapper.PaymentMapper;
import com.telran.store.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public Payment pay(PaymentCreateDto paymentCreateDto) {
        Order order = orderService.getById(paymentCreateDto.getOrderId());
        BigDecimal totalPrice = orderService.getTotalAmount(order);

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new RuntimeException("Order has already been paid");
        }

        if (order.getPaymentAmount().add(paymentCreateDto.getAmount()).compareTo(totalPrice) > 0) {
            throw new RuntimeException("The amount of payment exceeds the total amount of the order");
        }

        order.setPaymentAmount(order.getPaymentAmount().add(paymentCreateDto.getAmount()));

        Payment payment = new Payment();
        payment.setUser(order.getShopUser());
        payment.setOrder(order);
        payment.setAmount(paymentCreateDto.getAmount());
        payment.setDateTime(LocalDateTime.now());

        order.getPayments().add(payment);
        order.setPaymentStatus(order.getPaymentAmount().compareTo(totalPrice) == 0 ? PaymentStatus.PAID
                : PaymentStatus.PARTIALLY_PAID);

        Order orderEntity = orderService.saveOrder(order);
        return orderEntity.getPayments().get(orderEntity.getPayments().size()-1);
    }

    @Override
    public List<PaymentResponseDto> getAll() {
        List<Payment> payments = paymentRepository.findAll();
        return paymentMapper.toDtoList(payments);
    }

    @Override
    public Payment updatePaymentStatus(Long paymentId, PaymentStatus paymentStatus) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return paymentRepository.save(payment);
    }
}
