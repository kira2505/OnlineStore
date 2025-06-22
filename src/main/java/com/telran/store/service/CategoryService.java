package com.telran.store.service;

import com.telran.store.dto.CategoryCreateDto;
import com.telran.store.entity.Category;

import java.util.List;

public interface CategoryService {

    Category save(Category category);

    List<Category> getAll();

    Category getById(long id);

    void deleteById(long id);

    Category edit(long id, CategoryCreateDto category);
}
