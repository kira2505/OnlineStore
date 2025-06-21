package com.telran.store.service;

import com.telran.store.dto.ProductCreateDto;
import com.telran.store.dto.ProductResponseDto;
import com.telran.store.entity.Category;
import com.telran.store.entity.Product;
import com.telran.store.exception.ProductNotFoundException;
import com.telran.store.mapper.ProductMapper;
import com.telran.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;

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

    @Override
    public Product edit(Long id, ProductCreateDto productCreateDto) {
        Product product = productRepository.findById(id)
                        .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));

        productMapper.updateProductFromDto(productCreateDto, product);

        if (productCreateDto.getCategory() != null && productCreateDto.getCategory().getCategoryId() != null) {
            Category category = categoryService.getById(productCreateDto.getCategory().getCategoryId());
            product.setCategory(category);
        }

        return productRepository.save(product);
    }
}
