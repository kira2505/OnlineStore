package com.telran.store.controller;

import com.telran.store.dto.PaymentCreateDto;
import com.telran.store.dto.PaymentResponseDto;
import com.telran.store.mapper.PaymentMapper;
import com.telran.store.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentMapper paymentMapper;

    @PostMapping("/pay")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDto pay(@RequestBody PaymentCreateDto request) {
        return paymentMapper.toDto(paymentService.pay(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAll());
    }

    @GetMapping("{orderId}")
    public List<PaymentResponseDto> getPaymentsById(@PathVariable Long orderId) {
        return paymentMapper.toDtoList(paymentService.getAllById(orderId));
    }
}
