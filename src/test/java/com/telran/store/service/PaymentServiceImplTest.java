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
import java.util.ArrayList;
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
        //Order already PAID (Заказ уже ОПЛАЧЕН)
        Order paidOrder = new Order();
        paidOrder.setId(1L);
        paidOrder.setPaymentStatus(PaymentStatus.PAID);
        paidOrder.setPaymentAmount(new BigDecimal("100"));
        paidOrder.setTotalAmount(new BigDecimal("100"));
        paidOrder.setShopUser(new ShopUser());

        PaymentCreateDto dto = new PaymentCreateDto();
        dto.setOrderId(paidOrder.getId());
        dto.setAmount(new BigDecimal("10"));

        when(orderService.getById(paidOrder.getId())).thenReturn(paidOrder);
        when(orderService.getTotalAmount(paidOrder)).thenReturn(paidOrder.getTotalAmount());

        RuntimeException exceptionForPaid = assertThrows(RuntimeException.class, () -> paymentServiceImpl.pay(dto));
        assertEquals("Order has already been paid", exceptionForPaid.getMessage());

        //Full payment changes status to PAID (Полная оплата меняет статус на ОПЛАЧЕНО)
        Order partialOrder = new Order();
        partialOrder.setId(2L);
        partialOrder.setPaymentAmount(new BigDecimal("50"));
        partialOrder.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
        partialOrder.setTotalAmount(new BigDecimal("100.00"));
        partialOrder.setShopUser(new ShopUser());
        partialOrder.setPayments(new ArrayList<>());

        when(orderService.getById(partialOrder.getId())).thenReturn(partialOrder);
        when(orderService.getTotalAmount(partialOrder)).thenReturn(partialOrder.getTotalAmount());
        when(orderService.saveOrder(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));


        //Successful payment (Успешный платеж)
        dto.setOrderId(partialOrder.getId());
        dto.setAmount(new BigDecimal("30"));

        Payment payment = paymentServiceImpl.pay(dto);
        assertNotNull(payment);
        assertEquals(new BigDecimal("30"), payment.getAmount());
        assertEquals(PaymentStatus.PARTIALLY_PAID, partialOrder.getPaymentStatus());
        assertEquals(new BigDecimal("80"), partialOrder.getPaymentAmount());

        dto.setAmount(new BigDecimal("20"));
        Payment fullPayment = paymentServiceImpl.pay(dto);
        assertNotNull(fullPayment);
        assertEquals(new BigDecimal("20"), fullPayment.getAmount());
        assertEquals(PaymentStatus.PAID, partialOrder.getPaymentStatus());
        assertEquals(new BigDecimal("100"), partialOrder.getPaymentAmount());

        //Overdue payment (Платеж с превышением суммы)
        Order partialOrderForOverpay = new Order();
        partialOrderForOverpay.setId(3L);
        partialOrderForOverpay.setPaymentAmount(new BigDecimal("80"));
        partialOrderForOverpay.setPaymentStatus(PaymentStatus.PARTIALLY_PAID);
        partialOrderForOverpay.setTotalAmount(new BigDecimal("100.00"));
        partialOrderForOverpay.setShopUser(new ShopUser());
        partialOrderForOverpay.setPayments(new ArrayList<>());

        when(orderService.getById(partialOrderForOverpay.getId())).thenReturn(partialOrderForOverpay);
        when(orderService.getTotalAmount(partialOrderForOverpay)).thenReturn(partialOrderForOverpay.getTotalAmount());

        dto.setOrderId(partialOrderForOverpay.getId());
        dto.setAmount(new BigDecimal("30"));

        RuntimeException exceptionForPartiallyPaid = assertThrows(RuntimeException.class, () -> paymentServiceImpl.pay(dto));
        assertTrue(exceptionForPartiallyPaid.getMessage().contains("Payment exceeds the remaining amount"));
        assertTrue(exceptionForPartiallyPaid.getMessage().contains("You need to pay only: 20.00"));
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