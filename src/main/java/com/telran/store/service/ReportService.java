package com.telran.store.service;

import com.telran.store.dto.ProductSalesDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ReportService {

    List<ProductSalesDTO> mostPurchased();

    List<ProductSalesDTO> mostCancelled();

    BigDecimal getProfit(long amount, String value);
}
