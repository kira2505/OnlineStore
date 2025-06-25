package com.telran.store.dto;

import com.telran.store.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {

    private List<CartResponseDto> cartItems;

    private BigDecimal totalPrice;
}
