package com.telran.store.service;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.dto.OrderItemCreateDto;
import com.telran.store.entity.*;
import com.telran.store.enums.PaymentStatus;
import com.telran.store.enums.Status;
import com.telran.store.exception.*;
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

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private ShopUserService shopUserService;

    @Mock
    private CartService cartService;

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
        createDto.setOrderItems(List.of(orderItemDto,  orderItemDtoTwo));

        when(shopUserService.getShopUser()).thenReturn(shopUser);
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.createOrder(createDto);

        assertNotNull(order);
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

        when(shopUserService.getShopUser()).thenReturn(shopUser);

        OrderCreateDto createDto = new OrderCreateDto();
        createDto.setDeliveryAddress("address");
        createDto.setDeliveryMethod("courier");
        createDto.setOrderItems(new ArrayList<>());

        assertThrows(EmptyCartException.class,
                () -> orderService.createOrder(createDto));

        verifyNoInteractions(orderRepository);
    }

    @Test
    void testProductNotFoundInCart() {
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

        when(shopUserService.getShopUser()).thenReturn(shopUser);

        assertThrows(CartItemNotFoundException.class, () -> {
            orderService.createOrder(createDto);
        });
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

        when(shopUserService.getShopUser()).thenReturn(shopUser);

        assertThrows(InsufficientProductQuantityException.class,
                () -> orderService.createOrder(orderCreateDto));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void testCreateOrderUserNotFound() {
        OrderCreateDto createDto = new OrderCreateDto();
        createDto.setDeliveryAddress("address");
        createDto.setDeliveryMethod("courier");
        createDto.setOrderItems(new ArrayList<>());

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

        when(shopUserService.getShopUser()).thenReturn(user);
        when(orderRepository.findAllByShopUserId(userId)).thenReturn(orders);

        List<Order> result = orderService.getAllOrdersCurrentUser();

        assertEquals(2, result.size());
        verify(shopUserService).getShopUser();
        verify(orderRepository).findAllByShopUserId(userId);
    }

    @Test
    void testGetAllOrdersIfUserNotFound() {
        when(shopUserService.getShopUser()).thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () -> orderService.getAllOrdersCurrentUser());

        verify(shopUserService).getShopUser();
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
        Order order = Order.builder().id(1L).build();

        when(orderRepository.save(order)).thenReturn(order);

        assertEquals(order, orderService.saveOrder(order));
    }

    @Test
    void testCancelOrderWithPendingPaidPayment() {
        ShopUser user = new ShopUser();
        user.setId(1L);

        long orderId = 1L;
        Order order = Order.builder().id(orderId).paymentStatus(PaymentStatus.PENDING_PAID).status(Status.NEW)
                .shopUser(user).build();

        when(shopUserService.getShopUser()).thenReturn(user);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.cancelOrder(orderId);

        assertEquals(Status.CANCELED, result.getStatus());
        assertEquals(PaymentStatus.CANCELED, result.getPaymentStatus());
    }

    @Test
    void testCancelOrderWithPaidPayment() {
        ShopUser user = new ShopUser();
        user.setId(2L);

        long orderId = 200L;
        Order order = Order.builder()
                .id(orderId)
                .paymentStatus(PaymentStatus.PAID)
                .status(Status.PROCESSING)
                .shopUser(user)
                .build();

        when(shopUserService.getShopUser()).thenReturn(user);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Order result = orderService.cancelOrder(orderId);

        assertEquals(Status.CANCELED, result.getStatus());
        assertEquals(PaymentStatus.REFUND, result.getPaymentStatus());
    }

    @Test
    void testCancelOrderByNotOwnerShouldThrow() {
        ShopUser user = new ShopUser();
        user.setId(3L);

        ShopUser orderOwner = new ShopUser();
        orderOwner.setId(99L);

        long orderId = 300L;
        Order order = Order.builder()
                .id(orderId)
                .paymentStatus(PaymentStatus.PAID)
                .status(Status.NEW)
                .shopUser(orderOwner)
                .build();

        when(shopUserService.getShopUser()).thenReturn(user);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(OrderNotFoundException.class, () -> orderService.cancelOrder(orderId));
    }

    @Test
    void testCancelOrderAlreadyCompleted() {
        ShopUser user = new ShopUser();
        user.setId(4L);

        long orderId = 400L;
        Order order = Order.builder()
                .id(orderId)
                .paymentStatus(PaymentStatus.PAID)
                .status(Status.COMPLETED)
                .shopUser(user)
                .build();

        when(shopUserService.getShopUser()).thenReturn(user);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(OrderAlreadyCompletedException.class, () -> orderService.cancelOrder(orderId));
    }

    @Test
    void testCancelOrderAlreadyCanceled() {
        ShopUser user = new ShopUser();
        user.setId(5L);

        long orderId = 500L;
        Order order = Order.builder()
                .id(orderId)
                .paymentStatus(PaymentStatus.PAID)
                .status(Status.CANCELED)
                .shopUser(user)
                .build();

        when(shopUserService.getShopUser()).thenReturn(user);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(OrderAlreadyCanceledException.class, () -> orderService.cancelOrder(orderId));
    }

    @Test
    void testGetTotalAmount() {
        OrderItem item1 = new OrderItem();
        item1.setPriceAtPurchase(new BigDecimal("1"));
        item1.setQuantity(2);

        OrderItem item2 = new OrderItem();
        item2.setPriceAtPurchase(new BigDecimal("1"));
        item2.setQuantity(3);

        Order order = new Order();
        order.setOrderItems(Arrays.asList(item1, item2));

        BigDecimal expected = new BigDecimal("5");

        assertEquals(0, expected.compareTo(orderService.getTotalAmount(order))); // сравниваем BigDecimal правильно
    }

    @Test
    void testGetTotalAmountIfItemsIsNull() {
        Order order = new Order();

        order.setOrderItems(null);
        BigDecimal expected = BigDecimal.ZERO;
        assertEquals(0, expected.compareTo(orderService.getTotalAmount(order)));
    }
}