package com.telran.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CategoryResponseDto {

    private Long categoryId;

    private String name;

    private List<ProductResponseDto> products;
}
