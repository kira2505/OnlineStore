package com.telran.store.controller;

import com.telran.store.dto.CategoryCreateDto;
import com.telran.store.dto.CategoryDto;
import com.telran.store.dto.CategoryResponseDto;
import com.telran.store.mapper.CategoryMapper;
import com.telran.store.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController implements CategoryApi {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryDto create(@Valid @RequestBody CategoryCreateDto categoryCreateDto){
        return categoryMapper.toDtoToCategory(categoryService.save(categoryMapper.toEntity(categoryCreateDto)));
    }

    @Override
    public List<CategoryResponseDto> getAll(){
        return categoryMapper.toDtoList(categoryService.getAll());
    }

    @Override
    public CategoryResponseDto getById(@PathVariable long id){
        return categoryMapper.toDto(categoryService.getById(id));
    }

    @Override
    public void deleteById(@PathVariable long id){
        categoryService.deleteById(id);
    }

    @Override
    public CategoryResponseDto edit(@PathVariable long id, @Valid @RequestBody CategoryCreateDto category){
        return categoryMapper.toDto(categoryService.edit(id, category));
    }
}
