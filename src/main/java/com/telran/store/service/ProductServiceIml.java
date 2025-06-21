package com.telran.store.service;

import com.telran.store.entity.Product;
import com.telran.store.exception.ProductNotFoundException;
import com.telran.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceIml implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAll(String category, BigDecimal minPrice, BigDecimal maxPrice, Boolean discount, String sortBy) {
        Specification<Product> specification = Specification.where(null);

        if (category != null){
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
        if (discount != null && discount == true) {
            specification =specification.and((root, query, cb) ->
                    cb.isNotNull(root.get("discountPrice")));
        }

        List<String> remainingFieldsSorting = List.of("id", "price", "name");
        if (!remainingFieldsSorting.contains(sortBy)) {
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
    }
}
