package com.telran.store.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.telran.store.dto.CategoryDto;
import com.telran.store.dto.ProductCreateDto;
import com.telran.store.dto.ProductResponseDto;
import com.telran.store.entity.Product;
import com.telran.store.entity.ShopUser;
import com.telran.store.mapper.ProductMapper;
import com.telran.store.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
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
        CategoryDto category = new CategoryDto(1L, "Electronics");

        ProductCreateDto createDto = new ProductCreateDto(
                "Laptop",
                "High performance laptop",
                new BigDecimal("1200.00"),
                "http://image.com/laptop.png",
                new BigDecimal("999.99"),
                category
        );

        Product product = new Product();
        product.setId(1L);

        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Laptop");
        responseDto.setDescription("High performance laptop");
        responseDto.setPrice(new BigDecimal("1200.00"));
        responseDto.setImageUrl("http://image.com/laptop.png");
        responseDto.setDiscountPrice(new BigDecimal("999.99"));

        when(productMapper.toEntity(any())).thenReturn(product);
        when(productService.create(any())).thenReturn(product);
        when(productMapper.toDto(any())).thenReturn(responseDto);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1200.00));
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

    @Test
    void testEditProduct() throws Exception {
        Long id = 1L;
        CategoryDto category = new CategoryDto(1L, "Electronics");

        ProductCreateDto createDto = new ProductCreateDto(
                "Laptop",
                "Updated description",
                new BigDecimal("1300.00"),
                "http://image.com/laptop-new.png",
                new BigDecimal("1100.00"),
                category
        );

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);

        ProductResponseDto updatedResponse = new ProductResponseDto();
        updatedResponse.setId(1L);
        updatedResponse.setName("Laptop");
        updatedResponse.setDescription("Updated description");
        updatedResponse.setPrice(new BigDecimal("1200.00"));
        updatedResponse.setImageUrl("http://image.com/laptop.png");
        updatedResponse.setDiscountPrice(new BigDecimal("999.99"));

        when(productService.edit(eq(id), any())).thenReturn(updatedProduct);
        when(productMapper.toDto(any())).thenReturn(updatedResponse);

        mockMvc.perform(put("/products/{product_id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    void testGetDailyProduct() throws Exception {
        Product product = Product.builder()
                .id(1L)
                .name("Flowerpot")
                .price(new BigDecimal("100.00"))
                .discountPrice(new BigDecimal("80.00"))
                .build();

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(1L)
                .name("Flowerpot")
                .price(new BigDecimal("100.00"))
                .discountPrice(new BigDecimal("80.00"))
                .build();

        when(productService.getDailyProduct()).thenReturn(product);
        when(productMapper.toDto(any())).thenReturn(dto);

        mockMvc.perform(get("/products/daily_product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Flowerpot"))
                .andExpect(jsonPath("$.price").value("100.0"))
                .andExpect(jsonPath("$.discountPrice").value("80.0"));
    }
}