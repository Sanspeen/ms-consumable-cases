package com.example.api.mock.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductSummaryDto(
        Long id,
        String sku,
        String name,
        BigDecimal price,
        boolean active,
        Instant createdAt
) {}