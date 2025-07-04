package com.telran.store.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopUserCreateDto {

    @NotBlank(message = "Name cannot be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name cannot be empty")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "The phone cannot be empty")
    @Pattern(regexp = "\\+\\d{1,3}\\d{9,10}", message = "Invalid phone format")
    private String phoneNumber;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=_-]).{8,}$",
            message = "The password must contain uppercase, lowercase letters, a number and a special character and be at least 8 characters long.")
    private String password;
}
