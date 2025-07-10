package com.telran.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.telran.store.dto.ProductSalesDTO;
import com.telran.store.dto.ReportRequestDto;
import com.telran.store.service.ReportService;
import com.telran.store.service.security.JwtFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void mostCancelled() throws Exception {
        List<ProductSalesDTO> products = List.of(new ProductSalesDTO(1L, "Product", 5));
        when(reportService.mostCancelled()).thenReturn(products);

        mockMvc.perform(get("/reports/most-cancelled"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("Product"))
                .andExpect(jsonPath("$[0].totalQuantity").value(5));
    }

    @Test
    void mostPurchased() throws Exception {
        List<ProductSalesDTO> products = List.of(new ProductSalesDTO(1L, "Product", 5));
        when(reportService.mostPurchased()).thenReturn(products);

        mockMvc.perform(get("/reports/top-sellers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].productId").value(1L))
                .andExpect(jsonPath("$[0].productName").value("Product"))
                .andExpect(jsonPath("$[0].totalQuantity").value(5));

    }

    @Test
    void getProfit() throws Exception{
        ReportRequestDto dto = new ReportRequestDto(5, "days");
        BigDecimal profit = new BigDecimal("1200");

        when(reportService.getProfit(any(ReportRequestDto.class))).thenReturn(profit);

        mockMvc.perform(post("/reports/profit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("1200"));
    }
}