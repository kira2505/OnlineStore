package com.telran.store.repository;

import com.telran.store.entity.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @EntityGraph(attributePaths = "cartItems")
    Cart findByUserId(Long userId);
}
