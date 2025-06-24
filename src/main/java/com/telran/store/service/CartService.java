package com.telran.store.service;

import com.telran.store.dto.AddToCartRequest;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
import com.telran.store.entity.ShopUser;

import java.util.List;

public interface CartService {

    Cart create(ShopUser user);

    Cart add(Long userId, AddToCartRequest cartRequest);

    Cart edit(Cart cart);

    List<CartItem> getById(Long id);

    void clearCart(Long id);

    void deleteById(Long id);
}
