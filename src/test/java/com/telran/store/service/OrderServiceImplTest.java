package com.telran.store.service;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.dto.OrderItemCreateDto;
import com.telran.store.entity.*;
import com.telran.store.enums.PaymentStatus;
import com.telran.store.enums.Status;
import com.telran.store.exception.EmptyCartException;
import com.telran.store.exception.InsufficientProductQuantityException;
import com.telran.store.exception.OrderNotFoundException;
import com.telran.store.exception.UserNotFoundException;
import com.telran.store.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ShopUserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testSuccessCreateOrder() {
        long userId = 1L;
        ShopUser shopUser = ShopUser.builder().id(userId).phoneNumber("12345").build();
        Cart cart = new Cart();
        shopUser.setCart(cart);

        Product product = new Product();
        product.setId(2L);
        product.setPrice(BigDecimal.valueOf(200));

        Product productTwo = new Product();
        productTwo.setId(1L);
        productTwo.setPrice(BigDecimal.valueOf(300));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(5);
        cartItem.setPrice(product.getPrice());

        CartItem cartItemTwo = new CartItem();
        cartItemTwo.setProduct(productTwo);
        cartItemTwo.setQuantity(3);
        cartItemTwo.setPrice(productTwo.getPrice());

        cart.setCartItems(new HashSet<>(Set.of(cartItem, cartItemTwo)));

        OrderItemCreateDto orderItemDto = new OrderItemCreateDto();
        orderItemDto.setProductId(product.getId());
        orderItemDto.setQuantity(5);

        OrderItemCreateDto orderItemDtoTwo = new OrderItemCreateDto();
        orderItemDtoTwo.setProductId(productTwo.getId());
        orderItemDtoTwo.setQuantity(1);

        OrderCreateDto createDto = new OrderCreateDto();
        createDto.setDeliveryAddress("address");
        createDto.setDeliveryMethod("courier");
        createDto.setOrderItems(List.of(orderItemDto, orderItemDtoTwo));

        when(userRepository.findById(userId)).thenReturn(Optional.of(shopUser));
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.createOrder(userId, createDto);

        assertNotNull(order);
        assertEquals(userId, order.getShopUser().getId());
        assertEquals(2, order.getOrderItems().size());
        assertEquals(Status.NEW, order.getStatus());
        assertEquals(PaymentStatus.PENDING_PAID, order.getPaymentStatus());
        assertEquals("address", order.getDeliveryAddress());
        assertEquals("courier", order.getDeliveryMethod());
    }

    @Test
    void testCreateOrderWithEmptyCart() {
        ShopUser shopUser = ShopUser.builder().id(1L).phoneNumber("12345").build();
        Cart cart = new Cart();
        cart.setCartItems(new HashSet<>());
        shopUser.setCart(cart);

        when(userRepository.findById(shopUser.getId())).thenReturn(Optional.of(shopUser));

        OrderCreateDto createDto = new OrderCreateDto();
        createDto.setDeliveryAddress("address");
        createDto.setDeliveryMethod("courier");
        createDto.setOrderItems(new ArrayList<>());

        assertThrows(EmptyCartException.class,
                () -> orderService.createOrder(shopUser.getId(), createDto));

        verify(userRepository).findById(shopUser.getId());
        verifyNoInteractions(orderRepository);
    }

    @Test
    void testProductNotFoundInCart(){
        long userId = 1L;
        ShopUser shopUser = ShopUser.builder().id(userId).phoneNumber("12345").build();
        Cart cart = new Cart();
        shopUser.setCart(cart);

        Product product = new Product();
        product.setId(2L);
        product.setPrice(BigDecimal.valueOf(200));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(5);
        cartItem.setPrice(product.getPrice());

        cart.setCartItems(new HashSet<>(Set.of(cartItem)));

        OrderItemCreateDto orderItemDto = new OrderItemCreateDto();
        orderItemDto.setProductId(3L);
        orderItemDto.setQuantity(3);

        OrderCreateDto createDto = new OrderCreateDto();
        createDto.setDeliveryAddress("address");
        createDto.setDeliveryMethod("courier");
        createDto.setOrderItems(List.of(orderItemDto));

        when(userRepository.findById(userId)).thenReturn(Optional.of(shopUser));
        when(orderRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.createOrder(userId, createDto);

        assertNotNull(order);
        assertEquals(0, order.getOrderItems().size());
        assertEquals(BigDecimal.ZERO, order.getTotalAmount());
    }

    @Test
    void testInsufficientProductQuantity() {
        long userId = 1L;
        ShopUser shopUser = ShopUser.builder().id(userId).phoneNumber("12345").build();
        Cart cart = new Cart();
        shopUser.setCart(cart);

        Product product = new Product();
        product.setId(2L);
        product.setPrice(BigDecimal.valueOf(200));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(5);
        cartItem.setPrice(product.getPrice());

        cart.setCartItems(new HashSet<>(Set.of(cartItem)));

        OrderItemCreateDto orderItemDto = new OrderItemCreateDto();
        orderItemDto.setProductId(product.getId());
        orderItemDto.setQuantity(6);

        OrderCreateDto createDto = new OrderCreateDto();
        createDto.setDeliveryAddress("address");
        createDto.setDeliveryMethod("courier");
        createDto.setOrderItems(List.of(orderItemDto));

        OrderCreateDto orderCreateDto = new OrderCreateDto();
        orderCreateDto.setDeliveryAddress("delivery");
        orderCreateDto.setDeliveryMethod("method");
        orderCreateDto.setOrderItems(List.of(orderItemDto));

        when(userRepository.findById(userId)).thenReturn(Optional.of(shopUser));

        assertThrows(InsufficientProductQuantityException.class,
                () -> orderService.createOrder(userId, orderCreateDto));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void testCreateOrderUserNotFound() {
        long userId = 1L;

        OrderCreateDto createDto = new OrderCreateDto();
        createDto.setDeliveryAddress("address");
        createDto.setDeliveryMethod("courier");
        createDto.setOrderItems(new ArrayList<>());

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> orderService.createOrder(userId, createDto));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void testGetOrderById() {
        Order order = Order.builder().id(1L).build();
        long invalidId = 123L;

        when(orderRepository.findById(invalidId)).thenReturn(Optional.empty());
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertEquals(order, orderService.getById(order.getId()));
        assertThrows(OrderNotFoundException.class, () -> orderService.getById(invalidId));
    }

    @Test
    void testGetAllOrders() {
        long userId = 1L;
        ShopUser user = ShopUser.builder().id(userId).build();
        List<Order> orders = Arrays.asList(
                Order.builder().id(1L).build(),
                Order.builder().id(2L).build()
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderRepository.findAllByShopUserId(userId)).thenReturn(orders);

        assertEquals(2, orderService.getAllOrders(userId).size());
        verify(userRepository).findById(userId);
        verify(orderRepository).findAllByShopUserId(userId);
    }

    @Test
    void testGetAllOrdersIfUserNotFound() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> orderService.getAllOrders(userId));

        verify(userRepository).findById(userId);
        verify(orderRepository, never()).findAllByShopUserId(anyLong());
    }

    @Test
    void testGetAllOrdersByState() {
        List<Order> orders = Arrays.asList(
                Order.builder().id(1L).status(Status.NEW).build(),
                Order.builder().id(2L).status(Status.NEW).build()
        );
        when(orderRepository.findAllByStatus(Status.NEW)).thenReturn(orders);

        List<Order> result = orderService.getAllByState(Status.NEW);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(order -> order.getStatus() == Status.NEW));

        verify(orderRepository).findAllByStatus(Status.NEW);
    }

    @Test
    void testGetAllOrdersByPaymentState() {
        List<Order> orders = Arrays.asList(
                Order.builder().id(1L).paymentStatus(PaymentStatus.PENDING_PAID).build(),
                Order.builder().id(2L).paymentStatus(PaymentStatus.PENDING_PAID).build()
        );
        when(orderRepository.findAllByPaymentStatus(PaymentStatus.PENDING_PAID)).thenReturn(orders);

        List<Order> result = orderService.getAllByPaymentState(PaymentStatus.PENDING_PAID);

        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(order -> order.getPaymentStatus() == PaymentStatus.PENDING_PAID));

        verify(orderRepository).findAllByPaymentStatus(PaymentStatus.PENDING_PAID);
    }

    @Test
    void updateOrderStatus() {
        long orderId = 1L;
        Order order = Order.builder().id(orderId).status(Status.NEW).build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order updatedOrder = orderService.updateOrderStatus(orderId, Status.SHIPPED);

        assertNotNull(updatedOrder);
        assertEquals(Status.SHIPPED, updatedOrder.getStatus());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrderPaymentStatus() {
        long orderId = 1L;
        Order order = Order.builder().id(orderId).paymentStatus(PaymentStatus.PENDING_PAID).build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order updatedOrder = orderService.updateOrderPaymentStatus(orderId, PaymentStatus.PAID);

        assertNotNull(updatedOrder);
        assertEquals(PaymentStatus.PAID, updatedOrder.getPaymentStatus());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(order);
    }

    @Test
    void testSaveOrder() {
        Order order  = Order.builder().id(1L).build();

        when(orderRepository.save(order)).thenReturn(order);

        assertEquals(order, orderService.saveOrder(order));
    }

    @Test
    void testCancelOrder() {
        Order order = Order.builder().id(1L).build();
        //
    }
}