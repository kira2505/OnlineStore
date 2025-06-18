package com.telran.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShopUserCreateDto {

    private String name;

    private String email;

    private String phoneNumber;
}
