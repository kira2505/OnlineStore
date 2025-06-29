package com.telran.store.repository;

import com.telran.store.entity.Order;
import com.telran.store.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByShopUserId(Long userId);

    List<Order> findAllByStatus(Status status);

}
