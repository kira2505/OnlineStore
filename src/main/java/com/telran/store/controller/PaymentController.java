package com.telran.store.controller;

import com.telran.store.dto.OrderPendingPaidDto;
import com.telran.store.dto.PaymentCreateDto;
import com.telran.store.dto.PaymentResponseDto;
import com.telran.store.mapper.PaymentMapper;
import com.telran.store.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController implements PaymentApi {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentMapper paymentMapper;

    @PostMapping("/pay")
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public PaymentResponseDto pay(@Valid @RequestBody PaymentCreateDto request) {
        return paymentMapper.toDto(paymentService.pay(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    @GetMapping("{orderId}")
    @Override
    public List<PaymentResponseDto> getPaymentsById(@PathVariable Long orderId) {
        return paymentMapper.toDtoList(paymentService.getAllById(orderId));
    }


    @GetMapping("/pending_orders")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<OrderPendingPaidDto> getOrdersWaitingMoreThan(@RequestParam int days) {
        return paymentService.getWaiting(days);
    }
}
