package com.telran.store.service;

import com.telran.store.dto.ProductCreateDto;
import com.telran.store.entity.Product;

import java.util.List;

public interface ProductService {

    Product create(Product product);

    List<Product> getAll();

    Product getById(long productId);

    void deleteById(long productId);

    Product edit(Long id, ProductCreateDto productCreateDto);
}
