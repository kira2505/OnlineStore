package com.telran.store.mapper;

import com.telran.store.dto.OrderItemCreateDto;
import com.telran.store.dto.OrderItemResponseDto;
import com.telran.store.dto.OrderResponseDto;
import com.telran.store.entity.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItem toEntity(OrderItemCreateDto orderItemCreateDto);

    OrderItemResponseDto toDto(OrderItem orderItem);

    List<OrderItemResponseDto> toDtoList(List<OrderItem> orderItems);
}
