package com.telran.store.mapper;

import com.telran.store.dto.ProductCreateDto;
import com.telran.store.dto.ProductResponseDto;
import com.telran.store.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

    void updateProductFromDto(ProductCreateDto dto, @MappingTarget Product product);

    Product toEntity(ProductCreateDto productCreateDto);

    ProductResponseDto toDto(Product product);

    List<ProductResponseDto> toDtoList(List<Product> productList);
}
