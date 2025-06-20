package com.telran.store.mapper;

import com.telran.store.dto.ProductCreateDto;
import com.telran.store.dto.ProductResponseDto;
import com.telran.store.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

    Product toEntity(ProductCreateDto productCreateDto);

    ProductResponseDto toDto(Product product);

    List<ProductResponseDto> toDtoList(List<Product> productList);
}
