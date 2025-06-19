package com.telran.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CategoryCreateDto {

    private String name;

    private List<ProductCreateDto> products;
}
