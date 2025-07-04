package com.telran.store.controller;

import com.telran.store.dto.ProductSalesDTO;
import com.telran.store.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/most-cancelled")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductSalesDTO> mostCancelled() {
        return reportService.mostCancelled();
    }

    @GetMapping("/top-sellers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductSalesDTO> mostPurchased() {
        return reportService.mostPurchased();
    }
}
