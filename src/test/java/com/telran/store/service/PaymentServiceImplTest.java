package com.telran.store.service;

import com.telran.store.dto.PaymentCreateDto;
import com.telran.store.dto.PaymentResponseDto;
import com.telran.store.entity.Order;
import com.telran.store.entity.Payment;
import com.telran.store.entity.Product;
import com.telran.store.entity.ShopUser;
import com.telran.store.enums.PaymentStatus;
import com.telran.store.mapper.PaymentMapper;
import com.telran.store.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentServiceImpl;

    @Test
    void testPay() {
        Order order = new Order();
        order.setId(1L);
        order.setPaymentAmount(new BigDecimal("80"));
        order.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setShopUser(new ShopUser());


        PaymentCreateDto paymentCreateDto = new PaymentCreateDto();
        paymentCreateDto.setOrderId(order.getId());
        paymentCreateDto.setAmount(new BigDecimal("30"));

        when(orderService.getById(order.getId())).thenReturn(order);
        when(orderService.getTotalAmount(order)).thenReturn(order.getTotalAmount());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentServiceImpl.pay(paymentCreateDto));

        assertTrue(exception.getMessage().contains("Payment exceeds the remaining amount"));
        assertTrue(exception.getMessage().contains("You need to pay only: 20.00"));
    }

    @Test
    void testGetAll() {
        List<Payment> payments = Arrays.asList(
                Payment.builder()
                        .id(1L).user(new ShopUser()).amount(BigDecimal.valueOf(100)).build(),
                Payment.builder()
                        .id(2L).user(new ShopUser()).amount(BigDecimal.valueOf(200)).build());

        List<PaymentResponseDto> dtos = Arrays.asList(
                new PaymentResponseDto(1L, new ShopUser().getId(), new Order().getId(), BigDecimal.valueOf(100), null),
                new PaymentResponseDto(2L, new ShopUser().getId(), new Order().getId(), BigDecimal.valueOf(200), null)
        );

        when(paymentRepository.findAll()).thenReturn(payments);
        when(paymentMapper.toDtoList(payments)).thenReturn(dtos);

        List<PaymentResponseDto> result = paymentServiceImpl.getAll();

        assertEquals(2, payments.size());
        assertEquals(BigDecimal.valueOf(100), result.get(0).getAmount());
        assertEquals(BigDecimal.valueOf(200), result.get(1).getAmount());
    }

    @Test
    void testGetAllById() {
        List<Payment> payments = Arrays.asList(
                Payment.builder()
                        .id(1L).user(new ShopUser()).amount(BigDecimal.valueOf(100)).build(),
                Payment.builder()
                        .id(2L).user(new ShopUser()).amount(BigDecimal.valueOf(200)).build());

        Order order = new Order();
        order.setId(1L);

        when(orderService.getById(order.getId())).thenReturn(order);
        when(paymentRepository.findAllByOrderId(order.getId())).thenReturn(payments);

        List<Payment> result = paymentServiceImpl.getAllById(order.getId());

        assertEquals(2, payments.size());
        assertEquals(BigDecimal.valueOf(100), result.get(0).getAmount());
        assertEquals(BigDecimal.valueOf(200), result.get(1).getAmount());
    }
}