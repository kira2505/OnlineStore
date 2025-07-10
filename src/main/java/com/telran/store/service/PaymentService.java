package com.telran.store.service;

import com.telran.store.dto.OrderPendingPaidDto;
import com.telran.store.dto.PaymentCreateDto;
import com.telran.store.dto.PaymentResponseDto;
import com.telran.store.entity.Payment;

import java.util.List;

public interface PaymentService {

    Payment pay(PaymentCreateDto paymentCreateDto);

    List<PaymentResponseDto> getAll();

    List<Payment> getAllById(Long orderId);

    List<OrderPendingPaidDto> getWaiting(int days);
}
