package com.telran.store.dto;

import com.telran.store.entity.OrderItem;
import com.telran.store.entity.ShopUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {

    private List<OrderItemCreateDto> orderItems;

    private String deliveryAddress;

    private String deliveryMethod;
}
