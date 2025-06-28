package com.telran.store.controller;

import com.telran.store.dto.AddToCartRequest;
import com.telran.store.dto.CartItemResponseDto;
import com.telran.store.dto.CartResponseDto;
import com.telran.store.mapper.CartMapper;
import com.telran.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartMapper cartMapper;


    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CartItemResponseDto add(@RequestHeader("userId") Long userId,
                                   @RequestBody AddToCartRequest request) {
        return cartMapper.toCartItemDto(cartService.add(userId, request));
    }

    @PatchMapping("/{user_id}")
    public CartResponseDto edit(@PathVariable("user_id") Long userId,
                                @RequestBody AddToCartRequest cartRequest) {
        return cartMapper.toDto(cartService.edit(userId, cartRequest));
    }

    @GetMapping("/{user_id}")
    public CartResponseDto getById(@PathVariable("user_id") Long userId) {
        return cartMapper.toDto(cartService.getById(userId));
    }

    @PutMapping("/clear/{user_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable("user_id") Long userId) {
        cartService.clearCart(userId);
    }

    @DeleteMapping("/{user_id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("user_id") Long userId) {
        cartService.deleteById(userId);
    }
}
