package com.telran.store.controller;

import com.telran.store.dto.AddToCartRequestDto;
import com.telran.store.dto.CartItemResponseDto;
import com.telran.store.dto.CartResponseDto;
import com.telran.store.mapper.CartMapper;
import com.telran.store.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public CartItemResponseDto add(@Valid @RequestBody AddToCartRequestDto request) {
        return cartMapper.toCartItemDto(cartService.add(request));
    }


    @PatchMapping
    public CartResponseDto edit(@Valid @RequestBody AddToCartRequestDto cartRequest) {
        return cartMapper.toDto(cartService.edit(cartRequest));
    }

    @GetMapping
    public CartResponseDto getById() {
        return cartMapper.toDto(cartService.getById());
    }

    @PutMapping("/clear/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart() {
        cartService.clearCart();
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById() {
        cartService.deleteById();
    }

    @DeleteMapping("/products/{product_id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteByUserId(@Valid @PathVariable("product_id") Long productId) {
        cartService.deleteCartItem(productId);
    }
}
