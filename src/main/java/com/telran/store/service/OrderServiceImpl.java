package com.telran.store.service;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.dto.OrderItemCreateDto;
import com.telran.store.entity.*;
import com.telran.store.enums.Status;
import com.telran.store.exception.*;
import com.telran.store.repository.OrderRepository;
import com.telran.store.repository.ProductRepository;
import com.telran.store.repository.ShopUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ProductService productService;

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
                continue;
            } else {
                orderItem.setProduct(cartItem.getProduct());

                BigDecimal discount = cartItem.getProduct().getDiscountPrice();

                if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
                    orderItem.setPriceAtPurchase(discount);
                } else {
                    orderItem.setPriceAtPurchase(cartItem.getPrice());
                }

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

            orderItem.setQuantity(orderItemCreateDto.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
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

    @Override
    public List<Order> getAllByState(Status status) {
        return orderRepository.findAllByStatus(status);
    }

    @Override
    public Order updateOrderStatus(Long orderId, Status status) {
        Order order = getById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
