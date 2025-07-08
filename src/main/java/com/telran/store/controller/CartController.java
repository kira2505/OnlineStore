package com.telran.store.controller;

import com.telran.store.dto.AddToCartRequestDto;
import com.telran.store.dto.CartItemResponseDto;
import com.telran.store.dto.CartResponseDto;
import com.telran.store.mapper.CartMapper;
import com.telran.store.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController implements CartApi{

    @Autowired
    private CartService cartService;

    @Autowired
    private CartMapper cartMapper;

    @Override
    public CartItemResponseDto add(@Valid @RequestBody AddToCartRequestDto request) {
        return cartMapper.toCartItemDto(cartService.add(request));
    }

    @Override
    public CartResponseDto edit(@Valid @RequestBody AddToCartRequestDto cartRequest) {
        return cartMapper.toDto(cartService.edit(cartRequest));
    }

    @Override
    public CartResponseDto getById() {
        return cartMapper.toDto(cartService.getById());
    }

    @Override
    public void clearCart() {
        cartService.clearCart();
    }

    @Override
    public void deleteById() {
        cartService.deleteById();
    }

    @Override
    public void deleteByUserId(@PathVariable("product_id") Long productId) {
        cartService.deleteCartItem(productId);
    }
}
