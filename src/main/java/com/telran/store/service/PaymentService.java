package com.telran.store.service;

import com.telran.store.dto.OrderPendingPaidDto;
import com.telran.store.dto.PaymentCreateDto;
import com.telran.store.dto.PaymentResponseDto;
import com.telran.store.entity.Order;
import com.telran.store.entity.Payment;
import com.telran.store.entity.Product;
import com.telran.store.enums.PaymentStatus;

import java.util.List;

public interface PaymentService {

    Payment pay(PaymentCreateDto paymentCreateDto);

    List<PaymentResponseDto> getAll();

    List<Payment> getAllById(Long orderId);

    List<OrderPendingPaidDto> getWaiting(int days);
}
