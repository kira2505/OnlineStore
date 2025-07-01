package com.telran.store.controller;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.dto.OrderResponseDto;
import com.telran.store.mapper.OrderMapper;
import com.telran.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @PostMapping("{userId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public OrderResponseDto createOrder(
            @PathVariable Long userId,
            @RequestBody OrderCreateDto orderCreateDto) {
        return orderMapper.toDto(orderService.createOrder(userId, orderCreateDto));
    }

    @GetMapping("{orderId}")
    public OrderResponseDto getById(Long orderId){
        return orderMapper.toDto(orderService.getById(orderId));
    }

    @GetMapping("/history/{userId}")
    public List<OrderResponseDto> getAllOrders(@PathVariable Long userId) {
        return orderMapper.toDtoList(orderService.getAllOrders(userId));
    }

    @PatchMapping("{orderId}")
    @ResponseStatus(value = HttpStatus.OK)
    public OrderResponseDto closeOrder(@PathVariable Long orderId) {
        return orderMapper.toDto(orderService.cancelOrder(orderId));
    }
}
