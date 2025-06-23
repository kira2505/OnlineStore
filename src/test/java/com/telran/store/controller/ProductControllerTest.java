package com.telran.store.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.telran.store.dto.ProductCreateDto;
import com.telran.store.dto.ProductResponseDto;
import com.telran.store.entity.Product;
import com.telran.store.entity.ShopUser;
import com.telran.store.mapper.ProductMapper;
import com.telran.store.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    @Test
    void testGetProductAll() throws Exception {
        List<Product> productList = Arrays.asList(
                Product.builder()
                        .id(1L).name("Flowerpot").build(),
                Product.builder()
                        .id(2L).name("Lopata").build());
        List<ProductResponseDto> productResponseDtos = productList.stream()
                .map(product -> new ProductResponseDto(product.getId(),
                        product.getName(), product.getDescription(), product.getPrice(),
                        product.getImageUrl(), product.getDiscountPrice())).toList();

        when(productService.getAll(any(), any(), any(), any(), any())).thenReturn(productList);
        when(productMapper.toDtoList(any())).thenReturn(productResponseDtos);

        String contentAsString = mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertTrue(true);
    }

    @Test
    void testCreateProduct() throws Exception {
        Product product = Product.builder().id(1L).name("Flowerpot").build();
        ProductResponseDto productResponseDto = new ProductResponseDto(1L, "Flowerpot",
                product.getDescription(), product.getPrice(),
                product.getImageUrl(), product.getDiscountPrice());

        when(productService.create(any(Product.class))).thenReturn(product);
        when(productMapper.toEntity(any(ProductCreateDto.class))).thenReturn(product);

        String productObject = objectMapper.writeValueAsString(productResponseDto);

        mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(String.valueOf(productObject)))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testGetProductById() throws Exception {
        Product product = Product.builder().id(1L).name("Flowerpot").build();
        ProductResponseDto productResponseDto = new ProductResponseDto(1L, "Flowerpot",
                product.getDescription(), product.getPrice(),
                product.getImageUrl(), product.getDiscountPrice());

        when(productService.getById(product.getId())).thenReturn(product);
        when(productMapper.toDto(any())).thenReturn(productResponseDto);

        mockMvc.perform(get("/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testDeleteProductById() throws Exception {
        Product product = Product.builder().id(1L).name("Flowerpot").build();

        doNothing().when(productService).deleteById(product.getId());

        String contentAsString = mockMvc.perform(delete("/products/" + product.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}