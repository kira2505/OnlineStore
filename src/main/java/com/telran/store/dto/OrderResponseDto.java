package com.telran.store.dto;

import com.telran.store.entity.OrderItem;
import com.telran.store.entity.ShopUser;
import com.telran.store.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
