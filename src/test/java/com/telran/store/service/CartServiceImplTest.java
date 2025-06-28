package com.telran.store.service;

import com.telran.store.dto.AddToCartRequest;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
import com.telran.store.entity.Product;
import com.telran.store.entity.ShopUser;
import com.telran.store.exception.CartNotFoundException;
import com.telran.store.repository.CartItemRepository;
import com.telran.store.repository.CartRepository;
import com.telran.store.repository.ProductRepository;
import com.telran.store.repository.ShopUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ShopUserRepository shopUserRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImpl cartServiceImpl;

    @ParameterizedTest
    @CsvSource({"true", "false"})
    void testCreateCart(boolean cartExists) {
        ShopUser user = new ShopUser();
        user.setId(1L);

        if (cartExists) {
            Cart existCart = new Cart();
            existCart.setUser(user);

            when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(existCart));

            Cart cart = cartServiceImpl.create(user);

            assertThat(cart).isSameAs(existCart);
            verify(cartRepository, never()).save(any());
        } else {
            when(cartRepository.findByUserId(1L)).thenReturn(null);
            when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Cart cart = cartServiceImpl.create(user);

            assertNotNull(cart);
            assertEquals(user, cart.getUser());
            assertNotNull(cart.getCartItems());
            assertTrue(cart.getCartItems().isEmpty());
            verify(cartRepository).save(cart);
        }
    }

    @Test
    void testAddProductToCart() {
        ShopUser user = new ShopUser();
        user.setId(1L);

        Product product = new Product();
        product.setId(2L);
        product.setPrice(BigDecimal.valueOf(100));

        AddToCartRequest newCartItemRequest = new AddToCartRequest();
        newCartItemRequest.setProductId(product.getId());
        newCartItemRequest.setQuantity(2);

        AddToCartRequest editCartItemRequest = new AddToCartRequest();
        editCartItemRequest.setProductId(product.getId());
        editCartItemRequest.setQuantity(10);

        when(shopUserRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(cartRepository.findByUserId(user.getId())).thenReturn(null);
        when(cartRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CartItem cartItem = cartServiceImpl.add(user.getId(), newCartItemRequest);

        assertNotNull(cartItem);
        assertEquals(2, cartItem.getQuantity());
        assertEquals(product, cartItem.getProduct());
        assertEquals(BigDecimal.valueOf(100), cartItem.getPrice());

        CartItem existCartItem = new CartItem();
        existCartItem.setProduct(product);
        existCartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>(List.of(existCartItem)));
        existCartItem.setCart(cart);

        when(cartRepository.findByUserId(user.getId())).thenReturn(cart);

        cartServiceImpl.add(user.getId(), editCartItemRequest);

        assertEquals(12, existCartItem.getQuantity());
        verify(cartItemRepository, atLeastOnce()).save(existCartItem);
    }

    @Test
    void testEditCart() {
        Long userId = 1L;

        CartItem cartItem = new CartItem();
        Product product = new Product();
        product.setId(2L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>(List.of(cartItem)));

        AddToCartRequest request =  new AddToCartRequest();
        request.setProductId(product.getId());
        request.setQuantity(10);

        when(cartRepository.findByUserId(userId)).thenReturn(cart);
        when(cartItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Cart editCart = cartServiceImpl.edit(userId, request);

        assertNotNull(editCart);
        assertEquals(10, cartItem.getQuantity());
    }

    @Test
    void testGetById() {
        Long userId = 1L;
        Cart expectedCart = new Cart();
        expectedCart.setId(100L);
        
        when(cartRepository.findByUserId(userId)).thenReturn(expectedCart);

        Cart result = cartServiceImpl.getById(userId);

        assertNotNull(result);
        assertEquals(expectedCart.getId(), result.getId());
        verify(cartRepository).findByUserId(userId);
    }

    @Test
    void testClearCart() {
        Long userId = 1L;

        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>(List.of(new CartItem(), new CartItem())));

        when(cartRepository.findByUserId(userId)).thenReturn(cart);
        when(cartRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        cartServiceImpl.clearCart(userId);

        assertEquals(0, cart.getCartItems().size());
        verify(cartRepository).save(cart);

    }

    @Test
    void testDeleteCart() {
        Long existUserId = 1L;
        Long missUserId = 2L;

        Cart existCart = new Cart();
        when(cartRepository.findByUserId(existUserId)).thenReturn(existCart);

        cartServiceImpl.deleteById(existUserId);

        verify(cartRepository).delete(existCart);

        when(cartRepository.findByUserId(missUserId)).thenReturn(null);

        CartNotFoundException exception = assertThrows(CartNotFoundException.class, () ->
                cartServiceImpl.deleteById(missUserId));

        assertEquals("Cart by user id 2not found", exception.getMessage());
    }
}