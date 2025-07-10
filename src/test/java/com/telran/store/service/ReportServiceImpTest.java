package com.telran.store.service;

import com.telran.store.dto.ProductSalesDTO;
import com.telran.store.dto.ReportRequestDto;
import com.telran.store.enums.Status;
import com.telran.store.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceImpTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ReportServiceImp reportService;

    @Test
    void testMostPurchased() {
        List<ProductSalesDTO> mockList = List.of(
                new ProductSalesDTO(1L,"Product 1", 10),
                new ProductSalesDTO(2L, "Product 2", 5)
        );
        Pageable pageable = PageRequest.of(0, 10);
        when(orderRepository.findProductSalesByStatus(Status.COMPLETED, pageable)).thenReturn(mockList);

        List<ProductSalesDTO> result = reportService.mostPurchased();

        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getProductName());
        verify(orderRepository).findProductSalesByStatus(Status.COMPLETED, pageable);
    }

    @Test
    void testMostCancelled() {
        List<ProductSalesDTO> mockList = List.of(
                new ProductSalesDTO(1L, "Product 1", 10),
                new ProductSalesDTO(2L, "Product 2", 5)
        );
        Pageable pageable = PageRequest.of(0, 10);
        when(orderRepository.findProductSalesByStatus(Status.CANCELED, pageable)).thenReturn(mockList);

        List<ProductSalesDTO> result = reportService.mostCancelled();

        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getProductName());
        verify(orderRepository).findProductSalesByStatus(Status.CANCELED, pageable);
    }

    @ParameterizedTest
    @ValueSource(strings = {"hours", "minutes", "days", "month", "years"})
    void testGetProfit(String value) {
        ReportRequestDto dto = new ReportRequestDto(5, value);

        BigDecimal expectedProfit = new BigDecimal("1000");

        when(orderRepository.getTotalProfit(any())).thenReturn(expectedProfit);

        BigDecimal result = reportService.getProfit(dto);

        assertEquals(expectedProfit, result);
        verify(orderRepository).getTotalProfit(any(LocalDateTime.class));
    }
}