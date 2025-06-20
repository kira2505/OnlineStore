package com.telran.store.mapper;

import com.telran.store.dto.CategoryCreateDto;
import com.telran.store.dto.CategoryDto;
import com.telran.store.dto.CategoryResponseDto;
import com.telran.store.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CategoryMapper {

    Category toEntity(CategoryCreateDto categoryCreateDto);

    CategoryResponseDto toDto(Category category);

    List<CategoryResponseDto> toDtoList(List<Category> categories);

    @Mapping(source = "categoryId", target = "id")
    Category toEntityToCategory(CategoryDto categoryDto);
}
