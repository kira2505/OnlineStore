package com.telran.store.controller;

import com.telran.store.dto.CategoryCreateDto;
import com.telran.store.dto.CategoryDto;
import com.telran.store.dto.CategoryResponseDto;
import com.telran.store.entity.Category;
import com.telran.store.mapper.CategoryMapper;
import com.telran.store.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto create(@RequestBody CategoryCreateDto categoryCreateDto){
        return categoryMapper.toDtoToCategory(categoryService.save(categoryMapper.toEntity(categoryCreateDto)));
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
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@PathVariable long id){
        categoryService.deleteById(id);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponseDto edit(@PathVariable long id, @RequestBody CategoryCreateDto category){
        return categoryMapper.toDto(categoryService.edit(id, category));
    }
}
