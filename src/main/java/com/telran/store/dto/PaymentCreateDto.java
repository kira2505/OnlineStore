package com.telran.store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateDto {

    @NotNull(message = "Order Id must not be empty")
    private Long orderId;

    @NotNull(message = "The entered amount must not be empty.")
    private BigDecimal amount;
}
