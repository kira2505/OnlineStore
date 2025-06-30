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

    @Scheduled(fixedRate = 60000)
    @Async
    public void cancelExpiredNewOrders() {
        for (Order order : orderService.getAllByState(Status.NEW)) {
            if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(20))) {
                orderService.updateOrderStatus(order.getId(), Status.CANCELED);
                log.info("New order with id " + order.getId() + " canceled due to expired payment period");
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    @Async
    public void startProcessingPaidOrders() {

    }
}
