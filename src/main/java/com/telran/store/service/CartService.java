package com.telran.store.service;

import com.telran.store.dto.AddToCartRequest;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
import com.telran.store.entity.ShopUser;

public interface CartService {

    Cart create(ShopUser user);

    CartItem add(Long userId, AddToCartRequest cartRequest);

    Cart edit(Long userId, AddToCartRequest cartRequest);

    Cart getById(Long userId);

    void clearCart(Long userId);

    void deleteById(Long userId);

    Cart save(Cart cart);
}
