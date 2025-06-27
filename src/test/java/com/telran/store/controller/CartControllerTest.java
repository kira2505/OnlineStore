package com.telran.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telran.store.dto.AddToCartRequest;
import com.telran.store.dto.CartItemResponseDto;
import com.telran.store.dto.CartResponseDto;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
import com.telran.store.mapper.CartMapper;
import com.telran.store.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    @MockBean
    private CartMapper cartMapper;

    @Test
    void testAddProductToCart() throws Exception {

        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(10L);
        request.setQuantity(2);

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setProductId(10L);
        cartItemResponseDto.setQuantity(2);

        when(cartService.add(eq(1L), any(AddToCartRequest.class))).thenReturn(new CartItem());
        when(cartMapper.toCartItemDto(any(CartItem.class))).thenReturn(cartItemResponseDto);

        mockMvc.perform(post("/carts")
                        .header("userId", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(10))
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    void testEditProductToCart() throws Exception {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(5L);
        request.setQuantity(3);

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setProductId(5L);
        cartItemResponseDto.setQuantity(3);
        cartItemResponseDto.setPricePerItem(Double.valueOf(("25.00")));

        CartResponseDto responseDto = new CartResponseDto();
        responseDto.setCartId(1L);
        responseDto.setCartItems(List.of(cartItemResponseDto));
        responseDto.setTotalPrice(new BigDecimal("75.00"));

        when(cartService.edit(eq(1L), any(AddToCartRequest.class))).thenReturn(new Cart());
        when(cartMapper.toDto(any(Cart.class))).thenReturn(responseDto);

        mockMvc.perform(patch("/carts/{user_id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":5,\"quantity\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1));
    }

    @Test
    void testGetById() throws Exception {
        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setProductId(100L);
        cartItemResponseDto.setQuantity(2);
        cartItemResponseDto.setPricePerItem(Double.valueOf("25.00"));

        CartResponseDto responseDto = new CartResponseDto();
        responseDto.setCartId(1L);
        responseDto.setCartItems(List.of(cartItemResponseDto));
        responseDto.setTotalPrice(new BigDecimal("50.00"));

        when(cartService.getById(1L)).thenReturn(new Cart());
        when(cartMapper.toDto(any(Cart.class))).thenReturn(responseDto);

        mockMvc.perform(get("/carts/{user_id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1))
                .andExpect(jsonPath("$.cartItems[0].productId").value(100))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(2))
                .andExpect(jsonPath("$.cartItems[0].pricePerItem").value(25.00))
                .andExpect(jsonPath("$.totalPrice").value(50.00));
    }

    @Test
    void testClearCart() throws Exception {
        mockMvc.perform(put("/carts/clear/{user_id}", 1L))
                .andExpect(status().isNoContent());

        verify(cartService).clearCart(1L);
    }

    @Test
    void testDeleteProductFromCart() throws Exception {
        mockMvc.perform(delete("/carts/{user_id}", 1L))
                .andExpect(status().isNoContent());

        verify(cartService).deleteById(1L);
    }
}