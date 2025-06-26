package com.telran.store.dto;

import com.telran.store.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemCreateDto {

    private int quantity;

    private Long productId;
}
