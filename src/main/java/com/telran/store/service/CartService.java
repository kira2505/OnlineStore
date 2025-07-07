package com.telran.store.service;

import com.telran.store.dto.AddToCartRequestDto;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
import com.telran.store.entity.ShopUser;

public interface CartService {

    Cart create(ShopUser user);

    CartItem add(AddToCartRequestDto cartRequest);

    Cart edit(AddToCartRequestDto cartRequest);

    Cart getById();

    void clearCart();

    void deleteById();

    void deleteCartItem(Long productId);

    Cart save(Cart cart);
}
