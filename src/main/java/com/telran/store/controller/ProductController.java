package com.telran.store.controller;

import com.telran.store.dto.ProductCreateDto;
import com.telran.store.dto.ProductResponseDto;
import com.telran.store.mapper.ProductMapper;
import com.telran.store.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController implements ProductApi {

    @Autowired
    public ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ProductResponseDto createProduct(@Valid @RequestBody ProductCreateDto dto) {
        return productMapper.toDto(productService.create(productMapper.toEntity(dto)));
    }

    @GetMapping
    @Override
    public List<ProductResponseDto> getAll(@RequestParam(name = "category", required = false) String category,
                                           @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
                                           @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
                                           @RequestParam(name = "discount", required = false) Boolean discount,
                                           @RequestParam(name = "sort", required = false, defaultValue = "id") String sort) {
        return productMapper.toDtoList(productService.getAll(category, minPrice, maxPrice, discount, sort));
    }

    @GetMapping("/{id}")
    @Override
    public ProductResponseDto getById(@PathVariable long id) {
        return productMapper.toDto(productService.getById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteById(@PathVariable long id) {
        productService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Override
    public ProductResponseDto edit(@PathVariable Long id, @Valid @RequestBody ProductCreateDto dto) {
        return productMapper.toDto(productService.edit(id, dto));
    }

    @GetMapping("/daily_product")
    @Override
    public ProductResponseDto getDailyProduct() {
        return productMapper.toDto(productService.getDailyProduct());
    }
}
