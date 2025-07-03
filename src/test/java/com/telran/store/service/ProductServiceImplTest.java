package com.telran.store.service;

import com.telran.store.dto.CategoryDto;
import com.telran.store.dto.ProductCreateDto;
import com.telran.store.entity.Category;
import com.telran.store.entity.Product;
import com.telran.store.exception.ProductNotFoundException;
import com.telran.store.mapper.ProductMapper;
import com.telran.store.repository.ProductRepository;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    void testGetAllProductsWithoutFilters() {

        List<Product> products = List.of(
                Product.builder()
                        .id(1L).name("Flowerpot").build());

        when(productRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(products);

        List<Product> result = productServiceIml.getAll(null, null, null, null, null);

        assertEquals(products, result);


        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(productRepository).findAll(any(Specification.class), sortCaptor.capture());

        Sort sort = sortCaptor.getValue();
        assertEquals("id", sort.getOrderFor("id").getProperty());
    }

    @Test
    void testGetAllWithCategoryOnly() {
        String category = "Electronics";

        when(productRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of());

        productServiceIml.getAll(category, null, null, null, "id");

        ArgumentCaptor<Specification<Product>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);

        verify(productRepository).findAll(specCaptor.capture(), sortCaptor.capture());

        assertNotNull(specCaptor.getValue());

        //cb.equal()
        Specification<Product> spec = specCaptor.getValue();

        Root<Product> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path<Object> categoryPath = mock(Path.class);
        Path<Object> namePath = mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("category")).thenReturn(categoryPath);
        when(categoryPath.get("name")).thenReturn(namePath);
        when(cb.equal(namePath, category)).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, query, cb);

        assertEquals(predicate, result);
        verify(cb).equal(namePath, category);
    }


    @Test
    void testGetAllWithMinPriceOnly() {
        BigDecimal minPrice = new BigDecimal("150.00");

        ArgumentCaptor<Specification<Product>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        when(productRepository.findAll(specCaptor.capture(), any(Sort.class))).thenReturn(List.of());

        productServiceIml.getAll(null, minPrice, null, null, "id");

        verify(productRepository).findAll(any(Specification.class), any(Sort.class));

        //cb.greaterThanOrEqualTo()
        Specification<Product> spec = specCaptor.getValue();

        Root<Product> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path<BigDecimal> pricePath = (Path) mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.<BigDecimal>get("price")).thenReturn(pricePath);
        when(cb.greaterThanOrEqualTo(pricePath, minPrice)).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, query, cb);

        assertEquals(predicate, result);
        verify(cb).greaterThanOrEqualTo(pricePath, minPrice);
    }

    @Test
    void testGetAllWithMaxPriceOnly() {
        BigDecimal maxPrice = new BigDecimal("150.00");

        ArgumentCaptor<Specification<Product>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        when(productRepository.findAll(specCaptor.capture(), any(Sort.class))).thenReturn(List.of());

        productServiceIml.getAll(null, null, maxPrice, null, "id");

        verify(productRepository).findAll(any(Specification.class), any(Sort.class));

        //cb.lessThanOrEqualTo()
        Specification<Product> spec = specCaptor.getValue();

        Root<Product> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path<BigDecimal> pricePath = (Path) mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.<BigDecimal>get("price")).thenReturn(pricePath);
        when(cb.lessThanOrEqualTo(pricePath, maxPrice)).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, query, cb);

        assertEquals(predicate, result);
        verify(cb).lessThanOrEqualTo(pricePath, maxPrice);
    }

    //discount = true
    @Test
    void testGetAllWithDiscountTrueOnly() {
        Boolean discount = true;

        ArgumentCaptor<Specification<Product>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        when(productRepository.findAll(specCaptor.capture(), any(Sort.class))).thenReturn(List.of());

        productServiceIml.getAll(null, null, null, discount, "id");

        verify(productRepository).findAll(any(Specification.class), any(Sort.class));

        //cb.isNotNull()
        Specification<Product> spec = specCaptor.getValue();

        Root<Product> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Path<Object> discountPath = (Path) mock(Path.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get("discountPrice")).thenReturn(discountPath);
        when(cb.isNotNull(discountPath)).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, query, cb);

        assertEquals(predicate, result);
        verify(cb).isNotNull(discountPath);
    }

    //discount = false
    @Test
    void testGetAllWithDiscountFalse() {
        when(productRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of());

        productServiceIml.getAll(null, null, null, false, "id");

        verify(productRepository).findAll(any(Specification.class), any(Sort.class));
    }

    @Test
    void testGetAllWithInvalidSortBy() {
        String invalidSortBy = "unknownField";

        when(productRepository.findAll(any(Specification.class), any(Sort.class))).thenReturn(List.of());

        productServiceIml.getAll(null, null, null, null, invalidSortBy);

        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(productRepository).findAll(any(Specification.class), sortCaptor.capture());

        Sort sort = sortCaptor.getValue();
        assertEquals("id", sort.getOrderFor("id").getProperty());
    }

    @Test
    void testCreateProduct() {
        Product createProduct = new Product().builder()
                .id(1L)
                .name("Flowerpot")
                .description("big")
                .build();

        when(productRepository.save(createProduct)).thenReturn(createProduct);

        Product product = productServiceIml.create(createProduct);
        assertEquals(createProduct, product);
        verify(productRepository).save(createProduct);

        long missingId = 5L;
        when(productRepository.findById(missingId)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productServiceIml.getById(missingId));
        assertEquals("Product with id 5 not found ", exception.getMessage());

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