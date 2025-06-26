package com.telran.store.mapper;

import com.telran.store.dto.CartItemResponseDto;
import com.telran.store.dto.CartResponseDto;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "pricePerItem", source = "price")
    CartItemResponseDto toCartItemDto(CartItem cartItem);

    @Mapping(target = "cartId", source = "id")
    @Mapping(target = "cartItems", source = "cartItems")
    @Mapping(target = "totalPrice", expression = "java(calculateTotal(cart))")
    CartResponseDto toDto(Cart cart);

    List<CartItemResponseDto> toCartItemDtoList(List<CartItem> cartItems);


    @AfterMapping
    default BigDecimal calculateTotal(Cart cart) {
        if (cart.getCartItems() == null) {
            return BigDecimal.ZERO;
        }
        return cart.getCartItems().stream()
                .map(cartItem -> cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
