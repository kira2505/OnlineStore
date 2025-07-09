package com.telran.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {

    private Long cartId;

    private Set<CartItemResponseDto> cartItems;

    private BigDecimal totalPrice;
}
