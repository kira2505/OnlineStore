package com.telran.store.dto;

import com.telran.store.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CategoryCreateDto {

    private String name;

    private List<Product> products;
}
