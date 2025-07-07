package com.telran.store.service;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.dto.OrderItemCreateDto;
import com.telran.store.entity.*;
import com.telran.store.enums.PaymentStatus;
import com.telran.store.enums.Status;
import com.telran.store.exception.*;
import com.telran.store.repository.OrderRepository;
import com.telran.store.repository.ShopUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShopUserRepository userRepository;

    @Autowired
    private ShopUserService shopUserService;

    @Override
    public Order createOrder(OrderCreateDto orderCreateDto) {
        ShopUser user = userRepository.findById(shopUserService.getShopUser().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Order order = new Order();
        order.setShopUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setDeliveryAddress(orderCreateDto.getDeliveryAddress());
        order.setContactPhone(user.getPhoneNumber());
        order.setDeliveryMethod(orderCreateDto.getDeliveryMethod());
        order.setStatus(Status.NEW);

        List<OrderItem> orderItems = new ArrayList<>();

        Set<CartItem> cartItems = user.getCart().getCartItems();
        if (cartItems.isEmpty()) {
            throw new EmptyCartException("No cart items found");
        }

        Map<Long, CartItem> cartItemMap = new HashMap<>();
        for (CartItem cartItem : cartItems) {
            cartItemMap.put(cartItem.getProduct().getId(), cartItem);
        }

        for (OrderItemCreateDto orderItemCreateDto : orderCreateDto.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            CartItem cartItem = cartItemMap.get(orderItemCreateDto.getProductId());

            if (cartItem == null) {
                throw new CartItemNotFoundException("Product with ID " + orderItemCreateDto.getProductId() + " is not in the cart");
            } else {
                orderItem.setProduct(cartItem.getProduct());

                if (cartItem.getQuantity() < orderItemCreateDto.getQuantity()) {
                    throw new InsufficientProductQuantityException("Not enough quantity for product ");
                }

                if (cartItem.getQuantity() - orderItemCreateDto.getQuantity() == 0) {
                    user.getCart().getCartItems().remove(cartItem);
                } else {
                    cartItem.setQuantity(cartItem.getQuantity() - orderItemCreateDto.getQuantity());
                    user.getCart().getCartItems().add(cartItem);
                }
            }
            orderItem.setPriceAtPurchase(cartItem.getPrice());
            orderItem.setQuantity(orderItemCreateDto.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        order.setTotalAmount(getTotalAmount(order));
        order.setPaymentStatus(PaymentStatus.PENDING_PAID);
        return orderRepository.save(order);
    }

    @Override
    public Order getById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()
                -> new OrderNotFoundException("Order with id " + orderId + " not found"));
    }

    @Override
    public List<Order> getAllByState(Status status) {
        return orderRepository.findAllByStatus(status);
    }

    @Override
    public List<Order> getAllByPaymentState(PaymentStatus paymentStatus) {
        return orderRepository.findAllByPaymentStatus(paymentStatus);
    }

    @Override
    public Order updateOrderStatus(Long orderId, Status status) {
        Order order = getById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrderPaymentStatus(Long orderId, PaymentStatus paymentStatus) {
        Order order = getById(orderId);
        order.setPaymentStatus(paymentStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId) {
        Order order = getById(orderId);

        if (Status.COMPLETED.equals(order.getStatus())) {
            throw new OrderAlreadyCompletedException("Order is already completed and cannot be canceled");
        }

        if (PaymentStatus.PENDING_PAID.equals(order.getPaymentStatus())) {
            order.setPaymentStatus(PaymentStatus.CANCELED);
        } else {
            order.setPaymentStatus(PaymentStatus.REFUND);
        }
        order.setStatus(Status.CANCELED);
        return orderRepository.save(order);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public BigDecimal getTotalAmount(Order order) {
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return order.getOrderItems().stream()
                .filter(item -> item.getPriceAtPurchase() != null)
                .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Order> getAllOrdersCurrentUser() {
        return orderRepository.findAllByShopUserId(shopUserService.getShopUser().getId());
    }
}
