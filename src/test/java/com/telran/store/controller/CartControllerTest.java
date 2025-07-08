package com.telran.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telran.store.dto.AddToCartRequestDto;
import com.telran.store.dto.CartItemResponseDto;
import com.telran.store.dto.CartResponseDto;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
import com.telran.store.entity.Product;
import com.telran.store.mapper.CartMapper;
import com.telran.store.service.CartService;
import com.telran.store.service.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {

    @MockBean
    private JwtFilter jwtFilter;

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
        AddToCartRequestDto request = new AddToCartRequestDto();
        request.setProductId(10L);
        request.setQuantity(2);

        CartItem cartItem = new CartItem();
        Product product = new Product();
        product.setId(10L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setProductId(10L);
        cartItemResponseDto.setQuantity(2);

        when(cartService.add(any(AddToCartRequestDto.class))).thenReturn(cartItem);
        when(cartMapper.toCartItemDto(any(CartItem.class))).thenReturn(cartItemResponseDto);

        mockMvc.perform(post("/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(10))
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    void testEditProductToCart() throws Exception {
        AddToCartRequestDto request = new AddToCartRequestDto();
        request.setProductId(5L);
        request.setQuantity(3);

        Cart cart = new Cart();
        cart.setId(1L);

        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setProductId(5L);
        cartItemResponseDto.setQuantity(3);
        cartItemResponseDto.setPricePerItem(Double.valueOf(("25.00")));

        CartResponseDto responseDto = new CartResponseDto();
        responseDto.setCartId(1L);
        responseDto.setCartItems(Set.of(cartItemResponseDto));
        responseDto.setTotalPrice(new BigDecimal("75.00"));

        when(cartService.edit(any(AddToCartRequestDto.class))).thenReturn(cart);
        when(cartMapper.toDto(any(Cart.class))).thenReturn(responseDto);

        mockMvc.perform(patch("/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
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
        responseDto.setCartItems(Set.of(cartItemResponseDto));
        responseDto.setTotalPrice(new BigDecimal("50.00"));

        when(cartService.getById()).thenReturn(new Cart());
        when(cartMapper.toDto(any(Cart.class))).thenReturn(responseDto);

        mockMvc.perform(get("/carts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1))
                .andExpect(jsonPath("$.cartItems[0].productId").value(100))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(2))
                .andExpect(jsonPath("$.cartItems[0].pricePerItem").value(25.00))
                .andExpect(jsonPath("$.totalPrice").value(50.00));
    }

    @Test
    void testClearCart() throws Exception {
        mockMvc.perform(put("/carts/clear/"))
                .andExpect(status().isNoContent());

        verify(cartService).clearCart();
    }

    @Test
    void testDeleteProductFromCart() throws Exception {
        mockMvc.perform(delete("/carts"))
                .andExpect(status().isNoContent());

        verify(cartService).deleteById();
    }

    @Test
    void testDeleteCartItemFromCart() throws Exception {
        mockMvc.perform(delete("/carts/products/{product_id}", 1L))
                .andExpect(status().isNoContent());

        verify(cartService).deleteCartItem( 1L);
    }
}