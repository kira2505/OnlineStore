package com.telran.store.mapper;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.dto.OrderResponseDto;
import com.telran.store.entity.Order;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    Order toEntity(OrderCreateDto orderCreateDto);

    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "totalPrice", ignore = true)
    OrderResponseDto toDto(Order order);

    List<OrderResponseDto> toDtoList(List<Order> orders);

    @AfterMapping
    default void calculateTotalPrice(Order order, @MappingTarget OrderResponseDto orderResponseDto) {
        if (order.getOrderItems() == null) {
            orderResponseDto.setTotalPrice(BigDecimal.ZERO);
        }
        BigDecimal totalSum = order.getOrderItems().stream()
                .map(orderItem -> orderItem.getPriceAtPurchase().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        orderResponseDto.setTotalPrice(totalSum);
    }
}
