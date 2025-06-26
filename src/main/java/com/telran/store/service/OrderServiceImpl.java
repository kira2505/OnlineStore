package com.telran.store.service;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.dto.OrderItemCreateDto;
import com.telran.store.entity.*;
import com.telran.store.enums.Status;
import com.telran.store.exception.*;
import com.telran.store.repository.OrderRepository;
import com.telran.store.repository.ShopUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShopUserRepository userRepository;

    @Override
    public Order createOrder(Long userId, OrderCreateDto orderCreateDto) {
        ShopUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Order order = new Order();
        order.setShopUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setDeliveryAddress(orderCreateDto.getDeliveryAddress());
        order.setContactPhone(user.getPhoneNumber());
        order.setDeliveryMethod(orderCreateDto.getDeliveryMethod());
        order.setStatus(Status.NEW);

        List<OrderItem> orderItems = new ArrayList<>();

        List<CartItem> cartItems = user.getCart().getCartItems();
        if (cartItems.isEmpty()) {
            throw new EmptyCartException("No cart items found");
        }

        for (OrderItemCreateDto orderItemCreateDto : orderCreateDto.getOrderItems()) {
            CartItem cartItem = cartItems.stream()
                    .filter(cItem -> cItem.getProduct().getId().equals(orderItemCreateDto.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new CartItemNotFoundException("Product not found"));

            if (cartItem.getQuantity() < orderItemCreateDto.getQuantity()) {
                throw new InsufficientProductQuantityException("Not enough quantity for product ");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(orderItemCreateDto.getQuantity());
            if(cartItem.getProduct().getDiscountPrice().compareTo(BigDecimal.ZERO) <= 0) {
                orderItem.setPriceAtPurchase(cartItem.getPrice());
            } else {
                orderItem.setPriceAtPurchase(cartItem.getProduct().getDiscountPrice());
            }
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            if(cartItem.getQuantity() - orderItemCreateDto.getQuantity() == 0) {
                user.getCart().getCartItems().remove(cartItem);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() - orderItemCreateDto.getQuantity());
                user.getCart().getCartItems().add(cartItem);
            }
        }

        order.setOrderItems(orderItems);
        return orderRepository.save(order);
    }


    @Override
    public Order getById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()
                -> new OrderNotFoundException("Order with id " + orderId + " not found"));
    }

    @Override
    public List<Order> getAllOrders(Long userId) {
        ShopUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return orderRepository.findAllByShopUserId(user.getId());
    }
}
