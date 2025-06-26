package com.telran.store.service;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.entity.*;
import com.telran.store.enums.Status;
import com.telran.store.exception.*;
import com.telran.store.repository.CartRepository;
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

    @Autowired
    private CartRepository cartRepository;

    @Override
    public Order createOrder(Long userId, Order orderDto) {
        ShopUser user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        Order order = new Order();
        order.setShopUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setContactPhone(user.getPhoneNumber());
        order.setStatus(Status.NEW);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : user.getCart().getCartItems()) {
            OrderItem item = new OrderItem();
            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
            item.setOrder(order);
            BigDecimal price = cartItem.getProduct().getPrice();
            item.setPriceAtPurchase(price);
            orderItems.add(item);
        }

        for (OrderItem orderItem : orderItems) {
            CartItem cItem = user.getCart().getCartItems().stream()
                    .filter(cartItem -> cartItem.getProduct().getId().equals(orderItem.getProduct().getId()))
                    .findFirst()
                    .orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));

            if(cItem.getQuantity() < orderItem.getQuantity()) {
                throw new InsufficientProductQuantityException("Insufficient product quantity");
            }

            cItem.setQuantity(cItem.getQuantity() - orderItem.getQuantity());

        }


        order.setOrderItems(orderItems);

        Order save = orderRepository.save(order);


        return save;
    }

    @Override
    public Order getById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()
                -> new OrderNotFoundException("Order with id " + orderId + " not found"));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order edit(Long orderId, OrderCreateDto orderCreateDto) {
        return null;
    }
}
