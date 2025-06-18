package com.telran.store.service;

import com.telran.store.entity.Product;
import com.telran.store.exception.ProductNotFoundException;
import com.telran.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceIml implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public Product getById(long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("Product with id " + productId + " not found "));
    }

    @Override
    public void deleteById(long productId) {
        productRepository.deleteById(productId);
    }
}
