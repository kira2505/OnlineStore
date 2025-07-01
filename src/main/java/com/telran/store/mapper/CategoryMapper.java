package com.telran.store.mapper;

import com.telran.store.dto.CategoryCreateDto;
import com.telran.store.dto.CategoryDto;
import com.telran.store.dto.CategoryResponseDto;
import com.telran.store.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CategoryMapper {

    Category toEntity(CategoryCreateDto categoryCreateDto);

    void toUpdateEntity(@MappingTarget Category category, CategoryCreateDto dto);

    CategoryResponseDto toDto(Category category);

    List<CategoryResponseDto> toDtoList(List<Category> categories);

    @Mapping(source = "categoryId", target = "id")
    Category toEntityToCategory(CategoryDto categoryDto);

    @Mapping(source = "id", target = "categoryId")
    CategoryDto toDtoToCategory(Category category);
}
