package com.telran.store.mapper;

import com.telran.store.dto.OrderItemCreateDto;
import com.telran.store.dto.OrderItemResponseDto;
import com.telran.store.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItem toEntity(OrderItemCreateDto orderItemCreateDto);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemResponseDto toDto(OrderItem orderItem);

    List<OrderItemResponseDto> toDtoList(List<OrderItem> orderItems);
}
