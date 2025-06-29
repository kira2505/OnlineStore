package com.telran.store.service;

import com.telran.store.dto.AddToCartRequestDto;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
import com.telran.store.entity.ShopUser;

public interface CartService {

    Cart create(ShopUser user);

    CartItem add(Long userId, AddToCartRequestDto cartRequest);

    Cart edit(Long userId, AddToCartRequestDto cartRequest);

    Cart getById(Long userId);

    void clearCart(Long userId);

    void deleteById(Long userId);

    void deleteCartItem(Long userId, Long productId);

    Cart save(Cart cart);
}
