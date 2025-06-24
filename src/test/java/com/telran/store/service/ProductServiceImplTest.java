package com.telran.store.service;

import com.telran.store.dto.CategoryDto;
import com.telran.store.dto.ProductCreateDto;
import com.telran.store.entity.Category;
import com.telran.store.entity.Product;
import com.telran.store.mapper.ProductMapper;
import com.telran.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productServiceIml;

    @Test
    void testGetAllProduct() {
        String category = "Phones";
        BigDecimal minPrice = new BigDecimal("100.00");
        BigDecimal maxPrice = new BigDecimal("200.00");
        Boolean discount = true;
        String sortBy = "price";
        List<Product> products = Arrays.asList(
                Product.builder()
                        .id(1L).name("Flowerpot").build(),
                Product.builder()
                        .id(2L).name("Lopata").build());

        when(productRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(products);

        List<Product> productList = productServiceIml.getAll(category, minPrice,
                maxPrice, discount, sortBy);

        assertEquals(2, productList.size());

        verify(productRepository, times(1)).findAll(any(Specification.class),
                any(Sort.class));
    }


    @Test
    void testCreateProduct() {
        Product createProduct = new Product().builder().id(1L).name("Flowerpot").description("big").build();

        when(productRepository.save(createProduct)).thenReturn(createProduct);

        Product product = productServiceIml.create(createProduct);
        assertEquals(createProduct, product);
    }

    @Test
    void testGetProductById() {
        Product productOne = new Product().builder().id(1L).name("Flowerpot").build();

        when(productRepository.findById(productOne.getId())).thenReturn(Optional.of(productOne));

        Product product = productServiceIml.getById(productOne.getId());
        assertEquals(productOne, product);
    }

    @Test
    void testDeleteProductById() {
        Product productOne = new Product().builder().id(1L).name("Flowerpot").build();

        doNothing().when(productRepository).deleteById(productOne.getId());

        productServiceIml.deleteById(productOne.getId());

        verify(productRepository, times(1)).deleteById(productOne.getId());
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product().builder().id(1L).name("Flowerpot").build();

        Category category = new Category().builder().id(1L).name("Phones").build();

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(1L);

        ProductCreateDto productCreateDto = new ProductCreateDto();
        productCreateDto.setName("Lopata");
        productCreateDto.setCategory(categoryDto);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(categoryService.getById(category.getId())).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        doAnswer(invocation -> {
            ProductCreateDto source = invocation.getArgument(0);
            Product target = invocation.getArgument(1);
            target.setName(source.getName());
            return null;
        }).when(productMapper).updateProductFromDto(any(), any());

        Product result = productServiceIml.edit(product.getId(), productCreateDto);

        verify(productMapper).updateProductFromDto(productCreateDto, product);
        verify(categoryService).getById(category.getId());
        verify(productRepository).save(product);

        assertEquals("Lopata", result.getName());
    }

}