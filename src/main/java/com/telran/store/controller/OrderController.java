package com.telran.store.controller;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.dto.OrderResponseDto;
import com.telran.store.entity.Order;
import com.telran.store.mapper.OrderMapper;
import com.telran.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @PostMapping("{cardId}")
    public OrderResponseDto createOrder(
            @PathVariable Long cardId,
            @RequestBody OrderCreateDto orderCreateDto){
        return orderMapper.toDto(orderService.createOrder(cardId, orderMapper.toEntity(orderCreateDto)));
    }

    @GetMapping("{orderId}")
    public OrderResponseDto getById(Long orderId){
        return orderMapper.toDto(orderService.getById(orderId));
    }

    @GetMapping
    public List<OrderResponseDto> getAllOrders(){
        return orderMapper.toDtoList(orderService.getAllOrders());
    }

    @PatchMapping
    public OrderResponseDto edit(Long orderId, OrderCreateDto orderCreateDto){
        return null;
    }
}
