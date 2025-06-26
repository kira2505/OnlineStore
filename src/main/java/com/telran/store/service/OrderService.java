package com.telran.store.service;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.entity.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(Long userId, Order Order);

    Order getById(Long orderId);

    List<Order> getAllOrders();

    Order edit(Long orderId, OrderCreateDto orderCreateDto);
}
