package com.telran.store.controller;

import com.telran.store.dto.CategoryCreateDto;
import com.telran.store.dto.CategoryResponseDto;
import com.telran.store.entity.Category;
import com.telran.store.mapper.CategoryMapper;
import com.telran.store.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CategoryService categoryService;

    @MockBean
    CategoryMapper categoryMapper;

    @Test
    void getAllCategories() throws Exception {
        List<Category> categories = Arrays.asList(
                Category.builder()
                        .id(1L).name("phones").build(),
                Category.builder()
                        .id(2L).name("headphones").build());
        List<CategoryResponseDto> categoryResponseDtos = categories.stream()
                .map(category -> new CategoryResponseDto(category.getId(),
                        category.getName(),
                        new ArrayList<>())).toList();

        when(categoryService.getAll()).thenReturn(categories);
        when(categoryMapper.toDtoList(any())).thenReturn(categoryResponseDtos);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testGetCategoryById() throws Exception {
        Category category = Category.builder().id(1L).name("phones").build();
        CategoryResponseDto categoryResponseDto =
                new CategoryResponseDto(1L, "phones",
                        new ArrayList<>());

        when(categoryService.getById(category.getId())).thenReturn(category);
        when(categoryMapper.toDto(any())).thenReturn(categoryResponseDto);

        mockMvc.perform(get("/categories/" + category.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testDeleteCategoryById() throws Exception {
        Category category = Category.builder().id(1L).name("phone").build();

        doNothing().when(categoryService).deleteById(category.getId());

        mockMvc.perform(delete("/categories/" + category.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void testCreateCategory() throws Exception {
        Category category = Category.builder().id(1L).name("phones").build();
        CategoryResponseDto categoryResponseDto =
                new CategoryResponseDto(1L, "phones", new ArrayList<>());

        when(categoryService.save(any())).thenReturn(category);
        when(categoryMapper.toDto(any())).thenReturn(categoryResponseDto);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"phones\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testEditCategory() throws Exception {
        Category existing = Category.builder().id(1L).name("headphones").build();

        CategoryCreateDto dto = new CategoryCreateDto();
        dto.setName("headphones");

        CategoryResponseDto responseDto = new CategoryResponseDto(existing.getId(), "headphones", new ArrayList<>());
        when(categoryService.edit(eq(existing.getId()),any(CategoryCreateDto.class))).thenReturn(existing);

        when(categoryMapper.toDto(any())).thenReturn(responseDto);
        mockMvc.perform(patch("/categories/{id}", existing.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"headphones\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("headphones"))
                .andExpect(jsonPath("$.id").value(existing.getId()));
    }

}