package com.telran.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopUserDto {

    @NotBlank(message = "Name cannot be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "The name must contain only Latin letters")
    private String name;

    @NotBlank(message = "The phone cannot be empty")
    @Pattern(regexp = "\\+\\d{1,3}\\d{9,10}", message = "Invalid phone format")
    private String phoneNumber;
}
