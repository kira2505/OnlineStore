package com.telran.store.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDto {

    @NotBlank(message = "Product name must not be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "The name must contain only Latin letters")
    private String name;

    @NotBlank(message = "Description must not be empty")
    private String description;

    @NotNull(message = "The price should not be empty")
    private BigDecimal price;

    @NotBlank(message = "Image URL must not be empty")
    private String imageUrl;

    private BigDecimal discountPrice;

    @Valid
    private CategoryDto category;
}
