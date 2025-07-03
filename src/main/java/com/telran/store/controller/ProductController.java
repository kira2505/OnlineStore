package com.telran.store.controller;


import com.telran.store.dto.ProductCreateDto;
import com.telran.store.dto.ProductResponseDto;
import com.telran.store.entity.Category;
import com.telran.store.mapper.ProductMapper;
import com.telran.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    public ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponseDto createProduct(@RequestBody ProductCreateDto dto) {
        return productMapper.toDto(productService.create(productMapper.toEntity(dto)));
    }

    @GetMapping
    public List<ProductResponseDto> getAll(@RequestParam(name = "category", required = false) String category,
                                           @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
                                           @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
                                           @RequestParam(name = "discount", required = false) Boolean discount,
                                           @RequestParam(name = "sort", required = false, defaultValue = "id") String sort) {
        return productMapper.toDtoList(productService.getAll(category, minPrice, maxPrice, discount, sort ));
    }

    @GetMapping("/{product_id}")
    public ProductResponseDto getById(@PathVariable(name = "product_id") long productId) {
        return productMapper.toDto(productService.getById(productId));
    }

    @DeleteMapping("/{product_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@PathVariable long product_id) {
        productService.deleteById(product_id);
    }

    @PatchMapping("/{product_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ProductResponseDto edit(@PathVariable(name = "product_id") Long id, @RequestBody ProductCreateDto dto) {
        return productMapper.toDto(productService.edit(id, dto));
    }
}
