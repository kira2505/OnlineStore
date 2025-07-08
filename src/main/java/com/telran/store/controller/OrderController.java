package com.telran.store.controller;

import com.telran.store.dto.OrderCreateDto;
import com.telran.store.dto.OrderResponseDto;
import com.telran.store.mapper.OrderMapper;
import com.telran.store.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController implements OrderApi {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    @ResponseStatus(value = HttpStatus.CREATED)
    public OrderResponseDto createOrder(@Valid @RequestBody OrderCreateDto orderCreateDto) {
        return orderMapper.toDto(orderService.createOrder(orderCreateDto));
    }

    @Override
    @ResponseStatus(value = HttpStatus.OK)
    public OrderResponseDto getById(@PathVariable Long orderId){
        return orderMapper.toDto(orderService.getById(orderId));
    }

    @Override
    @ResponseStatus(value = HttpStatus.OK)
    public List<OrderResponseDto> getAllOrders() {
        return orderMapper.toDtoList(orderService.getAllOrdersCurrentUser());
    }

    @Override
    @ResponseStatus(value = HttpStatus.OK)
    public OrderResponseDto closeOrder(@PathVariable Long orderId) {
        return orderMapper.toDto(orderService.cancelOrder(orderId));
    }
}
