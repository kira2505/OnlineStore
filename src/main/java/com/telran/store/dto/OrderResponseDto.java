package com.telran.store.dto;

import com.telran.store.enums.PaymentStatus;
import com.telran.store.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private Long id;

    private String contactPhone;

    private String deliveryAddress;

    private String deliveryMethod;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Status status;

    private List<OrderItemResponseDto> orderItems;

    private BigDecimal totalPrice;

    private PaymentStatus paymentStatus;
}
