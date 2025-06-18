package com.telran.store.controller;

import com.telran.store.dto.CategoryCreateDto;
import com.telran.store.dto.CategoryResponseDto;
import com.telran.store.mapper.CategoryMapper;
import com.telran.store.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto create(@RequestBody CategoryCreateDto categoryCreateDto){
        return categoryMapper.toDto(categoryService.create(categoryMapper.toEntity(categoryCreateDto)));
    }

    @GetMapping
    public List<CategoryResponseDto> getAll(){
        return categoryMapper.toDtoList(categoryService.getAll());
    }

    @GetMapping("{id}")
    public CategoryResponseDto getById(@PathVariable long id){
        return categoryMapper.toDto(categoryService.getById(id));
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable long id){
        categoryService.deleteById(id);
    }
}
