package com.telran.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductCreateDto {

    private String name;

    private String description;

    private BigDecimal price;

    private String imageURL;

    private BigDecimal discountPrice;
}
