package com.telran.store.service;

import com.telran.store.entity.Category;

import java.util.List;

public interface CategoryService {

    Category create(Category category);

    List<Category> getAll();

    Category getById(long id);

    void deleteById(long id);
}
