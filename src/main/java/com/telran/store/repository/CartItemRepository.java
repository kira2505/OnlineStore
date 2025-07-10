package com.telran.store.repository;

import com.telran.store.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCart_IdAndProduct_Id(Long cartId, Long productId);
}
