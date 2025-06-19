package com.telran.store.service;

import com.telran.store.entity.Product;
import com.telran.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImlTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceIml productServiceIml;

    @Test
    void testGetAllProduct() {
        List<Product> products = Arrays.asList(
                Product.builder()
                        .id(1L).name("Flowerpot").build(),
                Product.builder()
                        .id(2L).name("Lopata").build());

        when(productRepository.findAll()).thenReturn(products);

        List<Product> productList = productServiceIml.getAll();
        assertEquals(2, productList.size());
    }

    @Test
    void testCreateProduct() {
        Product createProduct = new Product().builder().id(1L).name("Flowerpot").description("big").build();

        when(productRepository.save(createProduct)).thenReturn(createProduct);

        Product product = productServiceIml.create(createProduct);
        assertEquals(createProduct, product);
    }

    @Test
    void testGetProductById() {
        Product productOne = new Product().builder().id(1L).name("Flowerpot").build();

        when(productRepository.findById(productOne.getId())).thenReturn(Optional.of(productOne));

        Product product = productServiceIml.getById(productOne.getId());
        assertEquals(productOne, product);
    }

    @Test
    void testDeleteProductById() {
        Product productOne = new Product().builder().id(1L).name("Flowerpot").build();

        doNothing().when(productRepository).deleteById(productOne.getId());

        productServiceIml.deleteById(productOne.getId());

        verify(productRepository, times(1)).deleteById(productOne.getId());
    }



}