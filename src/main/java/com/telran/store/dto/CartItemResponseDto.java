package com.telran.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDto {

    private Long productId;

    private String productName;

    private Integer quantity;

    private Double pricePerItem;
}
