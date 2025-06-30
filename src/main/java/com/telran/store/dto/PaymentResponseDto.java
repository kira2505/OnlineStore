package com.telran.store.dto;

import com.telran.store.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {

    private Long id;

    private Long userId;

    private Long orderId;

    private BigDecimal amount;

    private LocalDateTime paymentDate;

    private PaymentStatus paymentStatus;
}
