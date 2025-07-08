package com.telran.store.service;

import com.telran.store.dto.CategoryCreateDto;
import com.telran.store.entity.Category;
import com.telran.store.exception.NoSuchCategoryException;
import com.telran.store.mapper.CategoryMapper;
import com.telran.store.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(long id) {
        return categoryRepository.findById(id).orElseThrow(()
                -> new NoSuchCategoryException("Category with ID " + id + " not found."));
    }

    @Override
    public void deleteById(long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category edit(long id, CategoryCreateDto category) {
        Category categoryById = getById(id);

        categoryMapper.toUpdateEntity(categoryById, category);
        return categoryRepository.save(categoryById);
    }
}
