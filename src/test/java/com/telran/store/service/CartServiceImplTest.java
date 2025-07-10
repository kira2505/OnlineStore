package com.telran.store.service;

import com.telran.store.dto.AddToCartRequestDto;
import com.telran.store.entity.Cart;
import com.telran.store.entity.CartItem;
import com.telran.store.entity.Product;
import com.telran.store.entity.ShopUser;
import com.telran.store.exception.CartItemNotFoundException;
import com.telran.store.exception.CartNotFoundException;
import com.telran.store.exception.ProductNotFoundException;
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

        when(shopUserService.getShopUser()).thenReturn(user);
        when(shopUserService.getById(user.getId())).thenReturn(user);
        when(productService.getById(product.getId())).thenReturn(product);
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(cartRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //New CartItem

        AddToCartRequestDto newCartItemRequest = new AddToCartRequestDto();
        newCartItemRequest.setProductId(product.getId());
        newCartItemRequest.setQuantity(2);

        CartItem addCartItem = cartServiceImpl.add(newCartItemRequest);

        assertNotNull(addCartItem);
        assertEquals(2, addCartItem.getQuantity());
        assertEquals(product, addCartItem.getProduct());
        assertEquals(BigDecimal.valueOf(100), addCartItem.getPrice());

        //Existing CartItem
        CartItem existCartItem = new CartItem();
        existCartItem.setProduct(product);
        existCartItem.setQuantity(2);
        existCartItem.setPrice(BigDecimal.valueOf(100));

        Product discountedProduct = new Product();
        discountedProduct.setId(3L);
        discountedProduct.setPrice(BigDecimal.valueOf(200));
        discountedProduct.setDiscountPrice(BigDecimal.valueOf(150));

        when(productService.getById(discountedProduct.getId())).thenReturn(discountedProduct);

        AddToCartRequestDto discountRequest = new AddToCartRequestDto();
        discountRequest.setProductId(discountedProduct.getId());
        discountRequest.setQuantity(1);

        CartItem discountedItem = cartServiceImpl.add(discountRequest);

        assertEquals(BigDecimal.valueOf(150), discountedItem.getPrice());

        Cart cart = new Cart();
        cart.setUser(user);
        existCartItem.setCart(cart);

        HashSet<CartItem> cartItems = new HashSet<>();
        cartItems.add(existCartItem);
        cart.setCartItems(cartItems);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        AddToCartRequestDto editCartItemRequest = new AddToCartRequestDto();
        editCartItemRequest.setProductId(product.getId());
        editCartItemRequest.setQuantity(3);

        CartItem update = cartServiceImpl.add(editCartItemRequest);

        assertNotNull(update);
        assertEquals(5, existCartItem.getQuantity());
        verify(cartItemRepository, atLeastOnce()).save(existCartItem);
    }

    @Test
    void testAddNewProductWithDiscount() {
        ShopUser user = new ShopUser();
        user.setId(1L);

        Product product = new Product();
        product.setId(3L);
        product.setPrice(BigDecimal.valueOf(100));
        product.setDiscountPrice(BigDecimal.valueOf(75));

        when(shopUserService.getShopUser()).thenReturn(user);
        when(shopUserService.getById(user.getId())).thenReturn(user);
        when(productService.getById(product.getId())).thenReturn(product);
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(cartRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(cartItemRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AddToCartRequestDto dto = new AddToCartRequestDto();
        dto.setProductId(product.getId());
        dto.setQuantity(1);

        CartItem result = cartServiceImpl.add(dto);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(75), result.getPrice());
        assertEquals(1, result.getQuantity());
    }

    @ParameterizedTest
    @CsvSource({"true", "false"})
    void testEditCart(boolean cartExists) {
        ShopUser user = new ShopUser();
        user.setId(1L);

        Product product = new Product();
        product.setId(2L);
        product.setPrice(BigDecimal.valueOf(100));

        Product existingProduct = new Product();
        existingProduct.setId(cartExists ? 2L : 999L);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(existingProduct);
        cartItem.setQuantity(2);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new HashSet<>(Collections.singletonList(cartItem)));

        lenient().when(shopUserService.getShopUser()).thenReturn(user);
        lenient().when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        lenient().when(productService.getById(product.getId())).thenReturn(product);

        AddToCartRequestDto dto = new AddToCartRequestDto();
        dto.setProductId(product.getId());
        dto.setQuantity(5);

        if (cartExists) {
            when(cartRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
            Cart result = cartServiceImpl.edit(dto);

            assertNotNull(result);
            CartItem updatedItem = result.getCartItems().stream()
                    .filter(ci -> ci.getProduct().getId().equals(product.getId()))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("Updated cart item not found"));

            assertEquals(5, updatedItem.getQuantity());
            assertEquals(product.getId(), updatedItem.getProduct().getId());

            verify(cartRepository).save(any(Cart.class));
        } else {
            ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () ->
                    cartServiceImpl.edit(dto)
            );
            assertTrue(exception.getMessage().contains("Product not found"));
            verify(cartItemRepository, never()).save(any());
        }
    }

    @Test
    void testGetById() {
        ShopUser user = new ShopUser();
        user.setId(1L);

        when(shopUserService.getShopUser()).thenReturn(user);

        Cart expectedCart = new Cart();
        expectedCart.setId(100L);
        
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(expectedCart));

        Cart result = cartServiceImpl.getById();

        assertNotNull(result);
        assertEquals(expectedCart.getId(), result.getId());
        verify(cartRepository).findByUserId(user.getId());
    }

    @Test
    void testClearCart() {
        ShopUser user = new ShopUser();
        user.setId(1L);

        Cart cart = new Cart();
        cart.setUser(user);
        HashSet<CartItem> cartItems = new HashSet<>();
        cartItems.add(new CartItem());
        cartItems.add(new CartItem());
        cart.setCartItems(cartItems);

        when(shopUserService.getShopUser()).thenReturn(user);
        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        cartServiceImpl.clearCart();

        assertTrue(cart.getCartItems().isEmpty(), "Cart items should be cleared");
        verify(shopUserService).getShopUser();
        verify(cartRepository).findByUserId(user.getId());
        verify(cartRepository).save(cart);
    }

    @Test
    void testDeleteCart() {
        ShopUser existUser = new ShopUser();
        existUser.setId(2L);

        when(shopUserService.getShopUser()).thenReturn(existUser);

        Cart existCart = new Cart();
        existCart.setUser(existUser);

        when(cartRepository.findByUserId(existUser.getId())).thenReturn(Optional.of(existCart));

        cartServiceImpl.deleteById();

        verify(cartRepository).delete(existCart);
        verify(shopUserService).getShopUser();

        when(cartRepository.findByUserId(existUser.getId())).thenReturn(Optional.empty());

        CartNotFoundException exception = assertThrows(CartNotFoundException.class, () ->
                cartServiceImpl.deleteById());

        assertEquals("Cart by user id 2 not found", exception.getMessage());
    }


    @Test
    void deleteCartItem_shouldRemoveItemSuccessfully() {
        ShopUser user = new ShopUser();
        user.setId(1L);
        when(shopUserService.getShopUser()).thenReturn(user);

        Long productId = 2L;
        Product product = new Product();
        product.setId(productId);

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new HashSet<>(Set.of(cartItem)));

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));

        cartServiceImpl.deleteCartItem(productId);

        assertFalse(cart.getCartItems().contains(cartItem), "CartItem should be removed");
        verify(shopUserService).getShopUser();
        verify(cartRepository).findByUserId(user.getId());
    }

    @Test
    void deleteCartItem_shouldThrowExceptionWhenItemNotFound() {
        ShopUser user = new ShopUser();
        user.setId(1L);
        when(shopUserService.getShopUser()).thenReturn(user);

        Long missingProductId = 999L;

        CartItem existingItem = new CartItem();
        Product existingProduct = new Product();
        existingProduct.setId(2L);
        existingItem.setProduct(existingProduct);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new HashSet<>(Set.of(existingItem)));

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));


        CartItemNotFoundException exception = assertThrows(CartItemNotFoundException.class, () ->
                cartServiceImpl.deleteCartItem(missingProductId));

        assertEquals("Cart item not found in cart", exception.getMessage());
        verify(shopUserService).getShopUser();
        verify(cartRepository).findByUserId(user.getId());
    }

    @Test
    void testSave() {
        Cart cart = new Cart();
        cart.setUser(new ShopUser());
        when(cartRepository.save(any())).thenReturn(cart);

        Cart savedCart = cartServiceImpl.save(cart);
        assertNotNull(savedCart);
        assertSame(cart, savedCart);
        verify(cartRepository).save(cart);
    }
}