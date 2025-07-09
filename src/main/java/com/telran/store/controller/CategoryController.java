package com.telran.store.controller;

import com.telran.store.dto.CategoryCreateDto;
import com.telran.store.dto.CategoryDto;
import com.telran.store.dto.CategoryResponseDto;
import com.telran.store.mapper.CategoryMapper;
import com.telran.store.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody CategoryCreateDto categoryCreateDto){
        return categoryMapper.toDtoToCategory(categoryService.save(categoryMapper.toEntity(categoryCreateDto)));
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponseDto> getAll(){
        return categoryMapper.toDtoList(categoryService.getAll());
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponseDto getById(@PathVariable long id){
        return categoryMapper.toDto(categoryService.getById(id));
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable long id){
        categoryService.deleteById(id);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public CategoryResponseDto edit(@PathVariable long id, @Valid @RequestBody CategoryCreateDto category){
        return categoryMapper.toDto(categoryService.edit(id, category));
    }
}
