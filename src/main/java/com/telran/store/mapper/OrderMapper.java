package com.telran.store.mapper;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.dto.OrderResponseDto;
import com.telran.store.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    Order toEntity(OrderCreateDto orderCreateDto);

    OrderResponseDto toDto(Order order);

    List<OrderResponseDto> toDtoList(List<Order> orders);

    void toUpdateEntity(@MappingTarget Order order, OrderCreateDto orderCreateDto);
}
