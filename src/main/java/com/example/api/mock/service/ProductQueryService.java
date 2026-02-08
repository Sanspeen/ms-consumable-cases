package com.example.api.mock.service;

import com.example.api.mock.dto.ProductSummaryDto;
import com.example.api.mock.model.product.Product;
import com.example.api.mock.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductQueryService {

    private final ProductRepository repo;

    public ProductQueryService(ProductRepository repo) {
        this.repo = repo;
    }

    public Page<ProductSummaryDto> find(Boolean active, String q, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Page<Product> page;

        boolean hasQ = q != null && !q.isBlank();
        boolean hasActive = active != null;
        boolean hasPriceRange = minPrice != null && maxPrice != null;

        if (hasActive && hasPriceRange) {
            page = repo.findByActiveAndPriceBetween(active, minPrice, maxPrice, pageable);
        } else if (hasPriceRange) {
            page = repo.findByPriceBetween(minPrice, maxPrice, pageable);
        } else if (hasActive) {
            page = repo.findByActive(active, pageable);
        } else if (hasQ) {
            String qq = q.trim();
            page = repo.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(qq, qq, pageable);
        } else {
            page = repo.findAll(pageable);
        }

        return page.map(this::toDto);
    }

    public ProductSummaryDto getById(Long id) {
        Product p = repo.findById(id).orElseThrow();
        return toDto(p);
    }

    private ProductSummaryDto toDto(Product p) {
        return new ProductSummaryDto(p.getId(), p.getSku(), p.getName(), p.getPrice(), p.isActive(), p.getCreatedAt());
    }
}