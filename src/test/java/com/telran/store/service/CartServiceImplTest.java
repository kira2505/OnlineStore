package com.telran.store.service;

import com.telran.store.dto.AddToCartRequestDto;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
import com.telran.store.entity.Product;
import com.telran.store.entity.ShopUser;
import com.telran.store.exception.CartItemNotFoundException;
import com.telran.store.exception.CartNotFoundException;
import com.telran.store.repository.CartItemRepository;
import com.telran.store.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ShopUserService shopUserService;

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
            existCart.setId(user.getId());
            existCart.setUser(user);

            when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(existCart));

            Cart cart = cartServiceImpl.create(user);

            assertThat(cart).isSameAs(existCart);
            verify(cartRepository, never()).save(any());
        } else {
            when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());
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

        AddToCartRequestDto newCartItemRequest = new AddToCartRequestDto();
        newCartItemRequest.setProductId(product.getId());
        newCartItemRequest.setQuantity(2);

        AddToCartRequestDto editCartItemRequest = new AddToCartRequestDto();
        editCartItemRequest.setProductId(product.getId());
        editCartItemRequest.setQuantity(10);

        when(shopUserService.getById(user.getId())).thenReturn(user);
        when(productService.getById(product.getId())).thenReturn(product);
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
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
        existCartItem.setPrice(BigDecimal.valueOf(100));

        Cart cart = new Cart();
        cart.setUser(user);
        existCartItem.setCart(cart);

        HashSet<CartItem> cartItems = new HashSet<>();
        cartItems.add(existCartItem);
        cart.setCartItems(cartItems);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        CartItem add = cartServiceImpl.add(user.getId(), editCartItemRequest);

        assertNotNull(add);
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

        HashSet<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);

        AddToCartRequestDto request =  new AddToCartRequestDto();
        request.setProductId(product.getId());
        request.setQuantity(10);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
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
        
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(expectedCart));

        Cart result = cartServiceImpl.getById(userId);

        assertNotNull(result);
        assertEquals(expectedCart.getId(), result.getId());
        verify(cartRepository).findByUserId(userId);
    }

    @Test
    void testClearCart() {
        Long userId = 1L;

        Cart cart = new Cart();

        HashSet<CartItem> cartItems = new HashSet<>();
        cartItems.add(new CartItem());
        cartItems.add(new CartItem());
        cart.setCartItems(cartItems);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
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
        when(cartRepository.findByUserId(existUserId)).thenReturn(Optional.of(existCart));

        cartServiceImpl.deleteById(existUserId);

        verify(cartRepository).delete(existCart);

        when(cartRepository.findByUserId(missUserId)).thenReturn(Optional.empty());

        CartNotFoundException exception = assertThrows(CartNotFoundException.class, () ->
                cartServiceImpl.deleteById(missUserId));

        assertEquals("Cart by user id 2 not found", exception.getMessage());
    }


    @Test
    void testDeleteCartItem() {
        Long userId = 1L;
        Long productId = 2L;
        Long missingProductId = 999L;

        Product product = new Product();
        product.setId(productId);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);

        Cart cart = new Cart();
        cart.setCartItems(new HashSet<>(Set.of(cartItem)));

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        cartServiceImpl.deleteCartItem(userId, productId);
        assertFalse(cart.getCartItems().contains(cartItem), "CartItem should be removed");

        CartItemNotFoundException exception = assertThrows(CartItemNotFoundException.class, () ->
                cartServiceImpl.deleteCartItem(userId, missingProductId));
        assertEquals("Cart item not found in cart", exception.getMessage());
    }

    @Test
    void testSave() {
        Cart cart = new Cart();
        when(cartRepository.save(any())).thenReturn(cart);

        Cart savedCart = cartServiceImpl.save(cart);
        assertNotNull(savedCart);
        assertSame(cart, savedCart);
        verify(cartRepository).save(cart);
    }
}