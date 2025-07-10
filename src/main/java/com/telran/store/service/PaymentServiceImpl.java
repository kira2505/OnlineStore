package com.telran.store.service;

import com.telran.store.dto.OrderPendingPaidDto;
import com.telran.store.dto.PaymentCreateDto;
import com.telran.store.dto.PaymentResponseDto;
import com.telran.store.entity.Order;
import com.telran.store.entity.Payment;
import com.telran.store.enums.PaymentStatus;
import com.telran.store.exception.AmountPaymentExceedsOrderTotalAmount;
import com.telran.store.exception.OrderAlreadyPaidException;
import com.telran.store.mapper.PaymentMapper;
import com.telran.store.repository.OrderRepository;
import com.telran.store.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public Payment pay(PaymentCreateDto paymentCreateDto) {
        Order order = orderService.getById(paymentCreateDto.getOrderId());
        BigDecimal totalPrice = orderService.getTotalAmount(order);

        if (PaymentStatus.PAID.equals(order.getPaymentStatus()) || PaymentStatus.COMPLETED.equals(order.getPaymentStatus())) {
            log.error("Order with ID: {} already has payment status: {}",  paymentCreateDto.getOrderId(),  order.getPaymentStatus());
            throw new OrderAlreadyPaidException("Order has already been paid");
        }

        if (order.getPaymentAmount().add(paymentCreateDto.getAmount()).compareTo(totalPrice) > 0) {
            BigDecimal remainingAmount = totalPrice.subtract(order.getPaymentAmount());
            log.error("Payment exceeds allowed amount. Order ID: {}, Trying to pay: {}, Remaining: {}",
                    order.getId(), paymentCreateDto.getAmount(), remainingAmount);
            throw new AmountPaymentExceedsOrderTotalAmount("The amount of payment exceeds the total amount of the order\n" +
                    "Payment exceeds the remaining amount. You need to pay only: " + remainingAmount);
        }

        order.setPaymentAmount(order.getPaymentAmount().add(paymentCreateDto.getAmount()));

        Payment payment = new Payment();
        payment.setUser(order.getShopUser());
        payment.setOrder(order);
        payment.setAmount(paymentCreateDto.getAmount());
        payment.setDateTime(LocalDateTime.now());

        log.debug("New payment created: amount = {}, userId = {}, orderId = {}, timestamp = {}",
                payment.getAmount(),
                payment.getUser().getId(),
                payment.getOrder().getId(),
                payment.getDateTime());

        order.getPayments().add(payment);
        order.setPaymentStatus(order.getPaymentAmount().compareTo(totalPrice) == 0 ? PaymentStatus.PAID
                : PaymentStatus.PARTIALLY_PAID);

        Order orderEntity = orderService.saveOrder(order);
        log.info("Payment of {} applied to order ID: {}. New paid amount: {}, Payment status: {}",
                payment.getAmount(),
                orderEntity.getId(),
                orderEntity.getPaymentAmount(),
                orderEntity.getPaymentStatus());
        return orderEntity.getPayments().get(orderEntity.getPayments().size() - 1);
    }

    @Override
    public List<PaymentResponseDto> getAll() {
        List<Payment> payments = paymentRepository.findAll();
        return paymentMapper.toDtoList(payments);
    }

    @Override
    public List<Payment> getAllById(Long orderId) {
        Order byId = orderService.getById(orderId);
        return paymentRepository.findAllByOrderId(byId.getId());
    }

    @Override
    public List<OrderPendingPaidDto> getWaiting(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusMinutes(days);
        List<Order> orders = orderRepository.findPendingPaymentOrderThen(cutoffDate);
        return orders.stream()
                .map(order -> {
                    long daysPending = ChronoUnit.MINUTES.between(order.getCreatedAt().toLocalTime(), LocalDateTime.now());
                    return new OrderPendingPaidDto(order.getId(), order.getCreatedAt(), daysPending);
                }).toList();
    }
}
