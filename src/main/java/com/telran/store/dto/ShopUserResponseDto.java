package com.telran.store.dto;

import com.telran.store.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopUserResponseDto {

    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private Role role;
}
