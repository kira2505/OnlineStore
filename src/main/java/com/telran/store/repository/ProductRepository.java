package com.telran.store.repository;

import com.telran.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Long id(Long id);
}
