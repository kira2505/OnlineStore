package com.telran.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopUserResponseDto {

    private Long id;

    private String name;

    private Set<FavoriteResponseDto> favorites;
}
