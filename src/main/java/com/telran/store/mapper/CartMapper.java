package com.telran.store.mapper;

import com.telran.store.dto.CartResponseDto;
import com.telran.store.entity.Cart;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartResponseDto toDto(Cart cart);

    List<CartResponseDto> toDtoList(List<Cart> carts);
}
