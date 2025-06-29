package com.telran.store.service;

import com.telran.store.entity.Order;
import com.telran.store.enums.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
                orderService.updateOrderStatus(order.getId(), Status.CANCELED);
            }
        }
        log.info("run from method one "  + LocalDateTime.now());
    }

    @Scheduled(fixedRate = 4000)
    @Async
    public void testMethodTwo() {
        log.info("run from method two "  + LocalDateTime.now());
    }
}
