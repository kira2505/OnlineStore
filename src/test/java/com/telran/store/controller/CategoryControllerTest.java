package com.telran.store.controller;

import com.telran.store.dto.*;
import com.telran.store.entity.Category;
import com.telran.store.mapper.CategoryMapper;
import com.telran.store.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
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
        Category category = Category.builder()
                .id(1L)
                .name("phones")
                .build();

        CategoryDto categoryDto = new CategoryDto(1L, "phones");

        when(categoryMapper.toEntity(any())).thenReturn(category);
        when(categoryService.save(any())).thenReturn(category);
        when(categoryMapper.toDtoToCategory(any())).thenReturn(categoryDto);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"phones\"}"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.name").value("phones"));
    }

    @Test
    void testEditCategory() throws Exception {
        Category category = Category.builder().id(1L).name("phones").build();

        CategoryCreateDto categoryCreateDto = new CategoryCreateDto();
        categoryCreateDto.setName("tvs");

        when(categoryService.edit(eq(1L), any(CategoryCreateDto.class))).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(new CategoryResponseDto());

        mockMvc.perform(patch("/categories/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"name\": \"tvs\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(categoryService).edit(eq(1L), any(CategoryCreateDto.class));
        verify(categoryMapper).toDto(category);
    }
}