package com.telran.store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequestDto {

    @NotNull(message = "Product Id must not be empty")
    private Long productId;

    @NotNull(message = "Quantity must not be empty")
    private Integer quantity;
}
