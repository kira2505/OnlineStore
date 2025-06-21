package com.telran.store.service;

import com.telran.store.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product create(Product product);

    List<Product> getAll(String category, BigDecimal minPrice, BigDecimal maxPrice, Boolean discount, String sortBy);

    Product getById(long productId);

    void deleteById(long productId);
}
