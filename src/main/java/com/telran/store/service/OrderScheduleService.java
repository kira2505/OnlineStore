package com.telran.store.service;

import com.telran.store.entity.Order;
import com.telran.store.entity.Payment;
import com.telran.store.enums.PaymentStatus;
import com.telran.store.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j //simple logger for java
@Service
public class OrderScheduleService {

    @Autowired
    private OrderService orderService;

    @Scheduled(fixedRate = 60000)
    @Async
    public void cancelExpiredNewOrders() {
        for (Order order : orderService.getAllByState(Status.NEW)) {
            if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(20))) {
                orderService.updateOrderStatus(order.getId(), Status.CANCELED);
                orderService.updateOrderPaymentStatus(order.getId(), PaymentStatus.CANCELED);
                log.info("New order with id " + order.getId() + " canceled due to expired payment period");
            }
        }
    }

    /*
    NEW,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELED,
    COMPLETED
     */

    /*
    NEW <-когда-> PENDING_PAID, PARTIALLY_PAID
PROCESSING когда PAID
SHIPPED
DELIVERED
CANCELED and PARTIALLY_PAID -> REFUND
CANCELED and PAID -> REFUND
CANCELED and PENDING_PAID -> CANCELED
COMPLETED -> (PAID -> COMPLETED)
     */

    @Scheduled(fixedRate = 60000)
    @Async
    public void setProcessingIfPaid() {
        for (Order order : orderService.getAllByState(Status.NEW)) {
            if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(2)) && order.getPaymentStatus() == PaymentStatus.PAID) {
                orderService.updateOrderStatus(order.getId(), Status.PROCESSING);
                log.info("Order with id " + order.getId() + " in processing");
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    @Async
    public void markAsShipped() {
        for (Order order : orderService.getAllByState(Status.PROCESSING)) {
            if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(2))) {
                orderService.updateOrderStatus(order.getId(), Status.SHIPPED);
                log.info("New order with id " + order.getId() + " send");
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    @Async
    public void markAsDelivered() {
        for (Order order : orderService.getAllByState(Status.SHIPPED)) {
            if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(2))) {
                orderService.updateOrderStatus(order.getId(), Status.DELIVERED);
                log.info("New order with id " + order.getId() + " delivered");
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    @Async
    public void refundPartialAndCancelOrder() {
        for (Order order : orderService.getAllByState(Status.DELIVERED)) {
            if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(2))) {
                orderService.updateOrderStatus(order.getId(), Status.COMPLETED);
                log.info("New order with id " + order.getId() + " completed");
            }
        }
    }
}
