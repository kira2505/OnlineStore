package com.telran.store.controller;

import com.telran.store.dto.AddToCartRequest;
import com.telran.store.dto.CartItemResponseDto;
import com.telran.store.dto.CartResponseDto;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
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
        CartItem cartItem = cartService.add(userId, request);
        return cartMapper.toCartItemDto(cartItem);
    }


    public Cart edit(Cart cart) {
        return null;
    }

    @GetMapping("/{user_id}")
    public CartResponseDto getById(@RequestHeader("userId") Long userId) {
        return cartMapper.toDto(cartService.getById(userId));
    }


    public void clearCart(Long id) {

    }

    @DeleteMapping("/{cart_id}")
    public void deleteById(Long id) {
        cartService.deleteById(id);
    }
}
