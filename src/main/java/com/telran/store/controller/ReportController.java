package com.telran.store.controller;

import com.telran.store.dto.ProductSalesDTO;
import com.telran.store.dto.ReportRequestDto;
import com.telran.store.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController implements  ReportApi {

    @Autowired
    private ReportService reportService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<ProductSalesDTO> mostCancelled() {
        return reportService.mostCancelled();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<ProductSalesDTO> mostPurchased() {
        return reportService.mostPurchased();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getProfit(@Valid @RequestBody ReportRequestDto reportRequestDto) {
        return reportService.getProfit(reportRequestDto);
    }
}
