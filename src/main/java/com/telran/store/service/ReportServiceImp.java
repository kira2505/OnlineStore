package com.telran.store.service;

import com.telran.store.dto.ProductSalesDTO;
import com.telran.store.dto.ReportRequestDto;
import com.telran.store.enums.Status;
import com.telran.store.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImp implements ReportService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<ProductSalesDTO> mostPurchased() {
        Pageable pageable = PageRequest.of(0, 10);
        List<ProductSalesDTO> page = orderRepository.findProductSalesByStatus(Status.COMPLETED, pageable);
        return page;
    }

    @Override
    public List<ProductSalesDTO> mostCancelled() {
        Pageable pageable = PageRequest.of(0, 10);
        List<ProductSalesDTO> page = orderRepository.findProductSalesByStatus(Status.CANCELED, pageable);
        return page;
    }

    @Override
    public BigDecimal getProfit(ReportRequestDto reportRequestDto) {
        LocalDateTime date = LocalDateTime.now();
        long amount = reportRequestDto.getAmount();

        switch (reportRequestDto.getValue().toLowerCase()) {
            case "hours" -> date = date.minusHours(amount);
            case "minutes" -> date = date.minusMinutes(amount);
            case "days" -> date = date.minusDays(amount);
            case "month" -> date = date.minusMonths(amount);
            case "years" -> date = date.minusYears(amount);
        }

        return orderRepository.getTotalProfit(date);
    }
}
