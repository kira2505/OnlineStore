package com.telran.store.controller;

import com.telran.store.dto.AddToCartRequest;
import com.telran.store.dto.CartResponseDto;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
import com.telran.store.mapper.CartMapper;
import com.telran.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartMapper cartMapper;

    @GetMapping
    public List<Cart> getAll() {
        return List.of();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CartResponseDto add(@RequestHeader("userId") Long userId,
                               @RequestBody AddToCartRequest request) {
        return cartMapper.toDto(cartService.add(userId, request));
    }


    public Cart edit(Cart cart) {
        return null;
    }

    @GetMapping("/{cart_id}")
    public List<CartItem> getById(Long id) {
        return List.of();
    }


    public void clearCart(Long id) {

    }

    @DeleteMapping("/{cart_id}")
    public void deleteById(Long id) {
        cartService.deleteById(id);
    }
}
