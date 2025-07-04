package com.telran.store.service;

import com.telran.store.dto.ProductSalesDTO;

import java.util.List;

public interface ReportService {

    List<ProductSalesDTO> mostPurchased();

    List<ProductSalesDTO> mostCancelled();
}
