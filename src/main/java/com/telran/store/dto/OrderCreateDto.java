package com.telran.store.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {

    @Valid
    @NotEmpty(message = "The Order Items list must not be empty.")
    private List<OrderItemCreateDto> orderItems;

    @NotBlank(message = "Shipping address must not be empty")
    private String deliveryAddress;

    @NotBlank(message = "Shipping method must not be empty")
    private String deliveryMethod;
}
