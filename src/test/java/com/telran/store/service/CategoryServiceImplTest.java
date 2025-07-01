package com.telran.store.service;

import com.telran.store.dto.CategoryCreateDto;
import com.telran.store.entity.Category;
import com.telran.store.exception.NoSuchCategoryException;
import com.telran.store.mapper.CategoryMapper;
import com.telran.store.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @Test
    void testGetAllCategories() {
        List<Category> categories = Arrays.asList(
                Category.builder().name("phones").id(1L).build(),
                Category.builder().name("headphones").id(2L).build());

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> categoriesList = categoryService.getAll();
        assertEquals(2, categoriesList.size());
    }

    @Test
    void testGetCategoryById() {
        Category category = Category.builder().id(1L).build();

        long invalidId = 999L;
        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        assertEquals(category, categoryService.getById(category.getId()));

        assertThrows(NoSuchCategoryException.class, () -> categoryService.getById(invalidId));

    }

    @Test
    void testDeleteCategoryById() {
        Category category = Category.builder().id(1L).build();

        doNothing().when(categoryRepository).deleteById(category.getId());

        categoryService.deleteById(category.getId());

        verify(categoryRepository, times(1)).deleteById(category.getId());
    }

    @Test
    void testCreateCategory() {
        Category category = Category.builder().id(1L).name("phones").build();

        when(categoryRepository.save(category)).thenReturn(category);

        assertEquals(category, categoryService.save(category));
    }

    @Test
    void testEditCategory() {
        Category category = Category.builder().id(1L).name("phones").build();

        CategoryCreateDto categoryCreateDto = new CategoryCreateDto();
        categoryCreateDto.setName("tvs");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenReturn(category);

        doAnswer(invocation -> {
            Category target = invocation.getArgument(0);
            CategoryCreateDto source = invocation.getArgument(1);
            target.setName(source.getName());
            return null;
        }).when(categoryMapper).toUpdateEntity(any(), any());

        Category result = categoryService.edit(1L, categoryCreateDto);

        verify(categoryMapper).toUpdateEntity(category, categoryCreateDto);
        verify(categoryRepository).save(category);

        assertEquals("tvs", result.getName());
    }
}