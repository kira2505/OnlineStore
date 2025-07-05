package com.telran.store.service;

import com.telran.store.dto.ProductSalesDTO;
import com.telran.store.enums.Status;
import com.telran.store.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImp implements ReportService{

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<ProductSalesDTO> mostPurchased() {
        return orderRepository.findProductSalesByStatus(Status.COMPLETED);
    }

    @Override
    public List<ProductSalesDTO> mostCancelled() {
        return orderRepository.findProductSalesByStatus(Status.CANCELED);
    }

    @Override
    public BigDecimal getProfit(long amount, String value) {
        LocalDateTime date = LocalDateTime.now();

        switch (value.toLowerCase()) {
            case "hours" -> date = date.minusHours(amount);
            case "minutes" -> date = date.minusMinutes(amount);
            case "days" -> date = date.minusDays(amount);
            case "months" -> date = date.minusMonths(amount);
            case "years" -> date = date.minusYears(amount);
            default -> throw new IllegalArgumentException("Unsupported time value. " +
                    "Must be one of: hours, minutes, days, months, years");
        }

        return orderRepository.getTotalProfit(date);
    }
}
