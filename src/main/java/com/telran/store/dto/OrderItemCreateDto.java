package com.telran.store.dto;

import com.telran.store.entity.Product;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemCreateDto {

    @NotNull(message = "Quantity must not be empty")
    private int quantity;

    @NotNull(message = "Product Id must not be empty")
    private Long productId;
}
