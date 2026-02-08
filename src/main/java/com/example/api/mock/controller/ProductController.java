package com.example.api.mock.controller;
import com.example.api.mock.dto.ProductSummaryDto;
import com.example.api.mock.service.ProductQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductQueryService service;

    public ProductController(ProductQueryService service) {
        this.service = service;
    }

    @GetMapping
    public Page<ProductSummaryDto> list(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable
    ) {
        return service.find(active, q, minPrice, maxPrice, pageable);
    }

    @GetMapping("/{id}")
    public ProductSummaryDto get(@PathVariable Long id) {
        return service.getById(id);
    }
}