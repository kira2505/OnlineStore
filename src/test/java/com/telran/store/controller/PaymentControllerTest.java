package com.telran.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telran.store.dto.OrderPendingPaidDto;
import com.telran.store.dto.PaymentCreateDto;
import com.telran.store.dto.PaymentResponseDto;
import com.telran.store.entity.Payment;
import com.telran.store.mapper.PaymentMapper;
import com.telran.store.service.PaymentService;
import com.telran.store.service.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {

    @MockBean
    private JwtFilter jwtFilter;

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

    @Test
    void testGetOrdersWaitingMoreThan() throws Exception {
        int days = 5;

        OrderPendingPaidDto dtoOne = new OrderPendingPaidDto(1L, LocalDateTime.now().minusDays(6), 6);
        OrderPendingPaidDto dtoTwo = new OrderPendingPaidDto(2L, LocalDateTime.now().minusDays(7), 7);

        List<OrderPendingPaidDto> list = List.of(dtoOne, dtoTwo);

        when(paymentService.getWaiting(days)).thenReturn(list);

        mockMvc.perform(get("/payments/pending_orders")
                        .param("days", String.valueOf(days))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(list.size()))
                .andExpect(jsonPath("$[0].orderId").value(dtoOne.getOrderId()))
                .andExpect(jsonPath("$[0].dayWaiting").value(dtoOne.getDayWaiting()))
                .andExpect(jsonPath("$[1].orderId").value(dtoTwo.getOrderId()))
                .andExpect(jsonPath("$[1].dayWaiting").value(dtoTwo.getDayWaiting()));

        verify(paymentService, times(1)).getWaiting(days);
    }
}