package com.telran.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateDto {

    @NotBlank(message = "Category name must not be empty")
    private String name;
}
