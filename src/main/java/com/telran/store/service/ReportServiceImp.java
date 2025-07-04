package com.telran.store.service;

import com.telran.store.dto.ProductSalesDTO;
import com.telran.store.enums.Status;
import com.telran.store.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
