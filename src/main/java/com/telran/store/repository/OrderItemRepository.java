package com.telran.store.repository;

import com.telran.store.entity.Order;
import com.telran.store.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
