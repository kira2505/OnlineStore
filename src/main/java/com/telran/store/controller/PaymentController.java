package com.telran.store.controller;

import com.telran.store.dto.OrderPendingPaidDto;
import com.telran.store.dto.PaymentCreateDto;
import com.telran.store.dto.PaymentResponseDto;
import com.telran.store.mapper.PaymentMapper;
import com.telran.store.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController implements PaymentApi {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public PaymentResponseDto pay(@Valid @RequestBody PaymentCreateDto request) {
        return paymentMapper.toDto(paymentService.pay(request));
    }

    @Override
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    @Override
    public List<PaymentResponseDto> getPaymentsById(@PathVariable Long orderId) {
        return paymentMapper.toDtoList(paymentService.getAllById(orderId));
    }

    @Override
    public List<OrderPendingPaidDto> getOrdersWaitingMoreThan(@RequestParam int days) {
        return paymentService.getWaiting(days);
    }
}
