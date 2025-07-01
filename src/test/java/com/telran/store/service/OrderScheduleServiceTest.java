package com.telran.store.service;

import com.telran.store.entity.Order;
import com.telran.store.enums.PaymentStatus;
import com.telran.store.enums.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderScheduleServiceTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderScheduleService orderScheduleService;

    @Test
    void testCancelExpiredNewOrders() {
        Order expiredPendingOrder = Order.builder().id(1L).status(Status.NEW).paymentStatus(PaymentStatus.PENDING_PAID)
                .createdAt(LocalDateTime.now().minusMinutes(21)).build();

        Order recentPendingOrder = Order.builder().id(2L).status(Status.NEW).paymentStatus(PaymentStatus.PENDING_PAID)
                .createdAt(LocalDateTime.now().minusMinutes(10)).build();

        Order expiredPaidOrder = Order.builder().id(3L).status(Status.NEW).paymentStatus(PaymentStatus.PAID)
                .createdAt(LocalDateTime.now().minusMinutes(25)).build();

        when(orderService.getAllByState(Status.NEW))
                .thenReturn(List.of(expiredPendingOrder, recentPendingOrder, expiredPaidOrder));

        orderScheduleService.cancelExpiredNewOrders();

        verify(orderService).updateOrderStatus(1L, Status.CANCELED);
        verify(orderService).updateOrderPaymentStatus(1L, PaymentStatus.CANCELED);

        verify(orderService, never()).updateOrderStatus(2L, Status.CANCELED);
        verify(orderService, never()).updateOrderPaymentStatus(2L, PaymentStatus.CANCELED);

        verify(orderService, never()).updateOrderStatus(3L, Status.CANCELED);
        verify(orderService, never()).updateOrderPaymentStatus(3L, PaymentStatus.CANCELED);

    }

    @Test
    void testSetProcessingIfPaid() {
        Order newPaidOrder = Order.builder().id(1L).status(Status.NEW).paymentStatus(PaymentStatus.PAID)
                .createdAt(LocalDateTime.now().minusMinutes(2)).build();

        Order newPartiallyPaidOrder = Order.builder().id(2L).status(Status.NEW).paymentStatus(PaymentStatus.PARTIALLY_PAID)
                .createdAt(LocalDateTime.now().minusMinutes(2)).build();

        when(orderService.getAllByState(Status.NEW))
                .thenReturn(List.of(newPaidOrder, newPartiallyPaidOrder));

        orderScheduleService.setProcessingIfPaid();
        verify(orderService).updateOrderStatus(1L, Status.PROCESSING);
        verify(orderService).updateOrderPaymentStatus(1L, PaymentStatus.COMPLETED);

        verify(orderService, never()).updateOrderStatus(2L, Status.PROCESSING);
        verify(orderService, never()).updateOrderPaymentStatus(2L, PaymentStatus.COMPLETED);
    }

    @Test
    void testMarkAsShipped() {
        Order order = Order.builder()
                .id(1L)
                .status(Status.PROCESSING)
                .createdAt(LocalDateTime.now().minusMinutes(3))
                .build();

        when(orderService.getAllByState(Status.PROCESSING)).thenReturn(List.of(order));
        when(orderService.updateOrderStatus(anyLong(), any())).thenReturn(order);

        orderScheduleService.markAsShipped();

        verify(orderService).updateOrderStatus(1L, Status.SHIPPED);
    }

    @Test
    void testMarkAsDelivered() {
        Order order = Order.builder()
                .id(1L)
                .status(Status.SHIPPED)
                .createdAt(LocalDateTime.now().minusMinutes(3))
                .build();

        when(orderService.getAllByState(Status.SHIPPED)).thenReturn(List.of(order));
        when(orderService.updateOrderStatus(anyLong(), any())).thenReturn(order);

        orderScheduleService.markAsDelivered();

        verify(orderService).updateOrderStatus(1L, Status.DELIVERED);
    }

    @Test
    void testMarkAsCompleted() {
        Order order = Order.builder()
                .id(1L)
                .status(Status.DELIVERED)
                .createdAt(LocalDateTime.now().minusMinutes(3))
                .build();

        when(orderService.getAllByState(Status.DELIVERED)).thenReturn(List.of(order));
        when(orderService.updateOrderStatus(anyLong(), any())).thenReturn(order);

        orderScheduleService.markAsCompleted();

        verify(orderService).updateOrderStatus(1L, Status.COMPLETED);
    }
}