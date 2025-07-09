package com.telran.store.service;

import com.telran.store.entity.Order;
import com.telran.store.enums.PaymentStatus;
import com.telran.store.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class OrderScheduleService {

    private static final int NUMBER_OF_MINUTES_FOR_PAUSE_BETWEEN_STATUS_CHANGE = 2;

    @Autowired
    private OrderService orderService;

    @Scheduled(fixedRate = 60000)
    @Async
    public void cancelExpiredNewOrders() {
        setStatusAndPStatus(Status.NEW, Status.CANCELED, PaymentStatus.PENDING_PAID, PaymentStatus.CANCELED);
    }

    @Scheduled(fixedRate = 60000)
    @Async
    public void setProcessingIfPaid() {
        setStatusAndPStatus(Status.NEW,Status.PROCESSING, PaymentStatus.PAID,PaymentStatus.COMPLETED);
    }

    @Scheduled(fixedRate = 60000)
    @Async
    public void markAsShipped() {
        setOrdersStatus(Status.PROCESSING, Status.SHIPPED);
    }

    @Scheduled(fixedRate = 60000)
    @Async
    public void markAsDelivered() {
        setOrdersStatus(Status.SHIPPED, Status.DELIVERED);
    }

    @Scheduled(fixedRate = 60000)
    @Async
    public void markAsCompleted() {
        setOrdersStatus(Status.DELIVERED, Status.COMPLETED);
    }

    private void setOrdersStatus(Status oldStatus, Status newStatus) {
        for (Order order : orderService.getAllByState(oldStatus)) {
            if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(NUMBER_OF_MINUTES_FOR_PAUSE_BETWEEN_STATUS_CHANGE))) {
                orderService.updateOrderStatus(order.getId(), newStatus);
                log.info("Order with id {} {}", order.getId(), order.getStatus());
            }
        }
    }

    private void setStatusAndPStatus(Status oldStatus, Status newStatus, PaymentStatus oldPaymentStatus, PaymentStatus newPaymentStatus) {
        for (Order order : orderService.getAllByState(oldStatus)) {
            if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(NUMBER_OF_MINUTES_FOR_PAUSE_BETWEEN_STATUS_CHANGE))
                    && order.getPaymentStatus() == oldPaymentStatus ){
                orderService.updateOrderStatus(order.getId(), newStatus);
                orderService.updateOrderPaymentStatus(order.getId(), newPaymentStatus);
                log.info("Order with id {} {}", order.getId(), order.getStatus());
            }
        }
    }
}
