package com.telran.store.service;

import com.telran.store.entity.Order;
import com.telran.store.entity.Payment;
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

    @Scheduled(fixedRate = 5000)
    @Async
    public void handleUnpaidOrders() {
        List<Order> allByState = orderService.getAllByState(Status.NEW);
        for (Order order : allByState) {
            if(order.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(60))) {
                orderService.updateOrderStatus(order.getId(), Status.PENDING_PAID);
            }
        }
        log.info("run from method one "  + LocalDateTime.now());
    }

    @Scheduled(fixedRate = 4000)
    @Async
    public void testMethodTwo() {
        log.info("run from method two "  + LocalDateTime.now());
    }

    @Scheduled(fixedRate = 6000)
    @Async
    public void handlePendingPaidOrders() {
        List<Order> pendingOrders = orderService.getAllByState(Status.PENDING_PAID);
        for (Order order : pendingOrders) {
            BigDecimal totalPaid = BigDecimal.ZERO;
            for (Payment payment : order.getPayments()) {
                totalPaid = totalPaid.add(payment.getAmount());
            }

            if (totalPaid.compareTo(order.getTotalPrice()) == 0) {
                orderService.updateOrderStatus(order.getId(), Status.PAID);
                log.info("Order " + order.getId() + " marked as PAID" + LocalDateTime.now());
            } else if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
                orderService.updateOrderStatus(order.getId(), Status.PARTIALLY_PAID);
                log.info("Order " + order.getId() + " marked as PARTIALLY PAID" + LocalDateTime.now());
            } else if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(10))) {
                orderService.updateOrderStatus(order.getId(), Status.CANCELED);
                log.info("Order marked as CANCELED (pending too long)" +  LocalDateTime.now());
            }
        }
    }


    @Scheduled(fixedRate = 7000)
    @Async
    public void handlePartiallyPaidOrders() {
        List<Order> partiallyPaidOrders = orderService.getAllByState(Status.PARTIALLY_PAID);
        for (Order order : partiallyPaidOrders) {
            BigDecimal totalPaid = BigDecimal.ZERO;
            for (Payment payment : order.getPayments()) {
                totalPaid = totalPaid.add(payment.getAmount());
            }

            if (totalPaid.compareTo(order.getTotalPrice()) == 0) {
                orderService.updateOrderStatus(order.getId(), Status.PAID);
                log.info("Order "+ order.getId() +" marked as PAID" + LocalDateTime.now());
            } else if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5))) {
                orderService.updateOrderStatus(order.getId(), Status.CANCELED);
                log.info("Order " + order.getId() + " marked as CANCELED due to timeout" +  LocalDateTime.now());
            }
        }
        log.info("run from method handlePartiallyPaidOrders " + LocalDateTime.now());
    }

    @Scheduled(fixedRate = 8000)
    @Async
    public void handlePaidOrders() {
        List<Order> paidOrders = orderService.getAllByState(Status.PAID);
        for (Order order : paidOrders) {
            if(order.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(60))) {
                orderService.updateOrderStatus(order.getId(), Status.PROCESSING);
            }
        }
        log.info("run from method handlePaidOrders "  + LocalDateTime.now());
    }
}
