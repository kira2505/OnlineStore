package com.telran.store.service;

import com.telran.store.entity.Payment;

import java.math.BigDecimal;

public interface PaymentService {

    void pay(BigDecimal totalPrice, Long orderId);
}
