package com.telran.store.service;

import com.telran.store.dto.ProductCreateDto;
import com.telran.store.entity.Category;
import com.telran.store.entity.Product;
import com.telran.store.exception.NotFoundProductWithDiscountPrice;
import com.telran.store.exception.ProductNotFoundException;
import com.telran.store.mapper.ProductMapper;
import com.telran.store.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product create(Product product) {
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
        Product savedProduct = productRepository.save(product);
        log.debug("Product created: {}", savedProduct);
        log.info("Product saved with id: {}", savedProduct.getId());
        return savedProduct;
    }

    @Override
    public List<Product> getAll(String category, BigDecimal minPrice, BigDecimal maxPrice, Boolean discount,
                                String sortBy) {
        Specification<Product> specification = Specification.where(null);

        if (category != null) {
            specification = specification.and((root, query, cb) ->
                    cb.equal(root.get("category").get("name"), category));
        }
        if (minPrice != null) {
            specification = specification.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            specification = specification.and(((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), maxPrice)));
        }
        if (discount != null && discount) {
            specification = specification.and((root, query, cb) ->
                    cb.isNotNull(root.get("discountPrice")));
        }

        List<String> remainingFieldsSorting = List.of("id", "price", "name");
        if (sortBy == null || !remainingFieldsSorting.contains(sortBy)) {
            sortBy = "id";
        }

        Sort sort = Sort.by(sortBy).ascending();

        return productRepository.findAll(specification, sort);
    }

    @Override
    public Product getById(long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("Product with id " + productId + " not found "));
    }

    @Override
    public void deleteById(long productId) {
        productRepository.deleteById(productId);
        log.info("Product with ID: {} was deleted", productId);
    }

    @Override
    public Product edit(Long id, ProductCreateDto productCreateDto) {
        Product product = getById(id);

        log.debug("Current product data before changes: {}", product);
        productMapper.updateProductFromDto(productCreateDto, product);

        if (productCreateDto.getCategory() != null) {
            Category category = categoryService.getById(productCreateDto.getCategory().getCategoryId());
            product.setCategory(category);
        }
        product.setUpdatedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        log.debug("Product edited: {}", savedProduct);
        return savedProduct;
    }

    @Override
    public Product getDailyProduct() {
        List<Product> dailyProducts = productRepository.findAll().stream()
                .filter(product -> product.getPrice() != null && product.getDiscountPrice() != null)
                .toList();

        if (dailyProducts.isEmpty()) {
            log.error("Product not found");
            throw new NotFoundProductWithDiscountPrice("There are no products with correct prices");
        }

        BigDecimal maxDiscount = dailyProducts.stream()
                .map(product -> product.getPrice()
                        .subtract(product.getDiscountPrice())
                        .divide(product.getPrice(), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)))
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        List<Product> topDiscount = dailyProducts.stream()
                .filter(product -> product.getPrice()
                        .subtract(product.getDiscountPrice())
                        .divide(product.getPrice(), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).compareTo(maxDiscount) == 0)
                .toList();

        int index = new Random().nextInt(topDiscount.size());
        Product product = topDiscount.get(index);
        log.info("Daily product is product with id: {}", product.getId());
        return product;
    }
}
