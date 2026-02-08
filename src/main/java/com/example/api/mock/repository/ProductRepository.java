package com.example.api.mock.repository;


import com.example.api.mock.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByActive(boolean active, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(
            String nameQ, String skuQ, Pageable pageable
    );

    Page<Product> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    Page<Product> findByActiveAndPriceBetween(boolean active, BigDecimal min, BigDecimal max, Pageable pageable);
}