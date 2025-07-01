package com.telran.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telran.store.dto.PaymentCreateDto;
import com.telran.store.dto.PaymentResponseDto;
import com.telran.store.entity.Payment;
import com.telran.store.mapper.PaymentMapper;
import com.telran.store.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private PaymentMapper paymentMapper;

    @Test
    void testPay() throws Exception {

        PaymentCreateDto paymentCreateDto = new PaymentCreateDto();
        paymentCreateDto.setOrderId(1L);
        paymentCreateDto.setAmount(new BigDecimal("50.00"));

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setId(1L);
        paymentResponseDto.setAmount(new BigDecimal("50.00"));

        Payment payment = new Payment();

        when(paymentService.pay(paymentCreateDto)).thenReturn(payment);
        when(paymentMapper.toDto(payment)).thenReturn(paymentResponseDto);

        mockMvc.perform(post("/payments/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(paymentResponseDto.getId()))
                .andExpect(jsonPath("$.amount").value(paymentResponseDto.getAmount().doubleValue()));

        verify(paymentService, times(1)).pay(paymentCreateDto);
        verify(paymentMapper, times(1)).toDto(payment);
    }

    @Test
    void testGetAllPayments() throws Exception {
        List<Payment> payments = Arrays.asList(new Payment(), new Payment());

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setId(1L);
        paymentResponseDto.setAmount(new BigDecimal("50.00"));

        List<PaymentResponseDto> dtoList = new ArrayList<>();
        dtoList.add(paymentResponseDto);

        when(paymentService.getAll()).thenReturn(dtoList);

        mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(paymentResponseDto.getId()))
                .andExpect(jsonPath("$[0].amount").value(paymentResponseDto.getAmount().doubleValue()));

        verify(paymentService, times(1)).getAll();
    }

    @Test
    void testGetPaymentsById() throws Exception {
        Long orderId = 1L;
        List<Payment> payments = Arrays.asList(new Payment(), new Payment());

        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        paymentResponseDto.setId(1L);
        paymentResponseDto.setAmount(new BigDecimal("50.00"));

        List<PaymentResponseDto> dtoList = new ArrayList<>();
        dtoList.add(paymentResponseDto);

        when(paymentService.getAllById(orderId)).thenReturn(payments);
        when(paymentMapper.toDtoList(payments)).thenReturn(dtoList);

        mockMvc.perform(get("/payments/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(paymentResponseDto.getId()))
                .andExpect(jsonPath("$[0].amount").value(paymentResponseDto.getAmount().doubleValue()));

        verify(paymentService, times(1)).getAllById(orderId);
        verify(paymentMapper, times(1)).toDtoList(payments);
    }
}