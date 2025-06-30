package com.telran.store.service;

import com.telran.store.dto.PaymentCreateDto;
import com.telran.store.dto.PaymentResponseDto;
import com.telran.store.entity.Payment;
import com.telran.store.enums.PaymentStatus;

import java.util.List;

public interface PaymentService {

    Payment pay(PaymentCreateDto paymentCreateDto);

    List<PaymentResponseDto> getAll();

    Payment updatePaymentStatus(Long paymentId, PaymentStatus paymentStatus);
}
