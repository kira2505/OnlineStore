package com.telran.store.service;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.entity.Order;
import com.telran.store.enums.PaymentStatus;
import com.telran.store.enums.Status;

import java.util.List;

public interface OrderService {

    Order createOrder(Long userId, OrderCreateDto orderCreateDto);

    Order getById(Long orderId);

    List<Order> getAllOrders(Long userId);

    List<Order> getAllByState(Status status);

    List<Order> getAllByPaymentState(PaymentStatus paymentStatus);

    Order updateOrderStatus(Long orderId, Status status);

    Order updateOrderPaymentStatus(Long orderId, PaymentStatus paymentStatus);
}
