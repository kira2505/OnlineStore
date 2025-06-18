package com.telran.store.controller;


import com.telran.store.dto.ProductCreateDto;
import com.telran.store.dto.ProductResponseDto;
import com.telran.store.mapper.ProductMapper;
import com.telran.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public ProductResponseDto createProduct(@RequestBody ProductCreateDto dto){
        return productMapper.toDto(productService.create(productMapper.toEntity(dto)));
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public List<ProductResponseDto> getAll(){
        return productMapper.toDtoList(productService.getAll());
    }

    @GetMapping("/{product_id}")
    public ProductResponseDto getById(@PathVariable(name = "product_id") long productId){
        return productMapper.toDto(productService.getById(productId));
    }

    @DeleteMapping("/{product_id}")
    public void deleteById(@PathVariable long product_id){
        productService.deleteById(product_id);
    }


}
