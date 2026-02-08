package com.example.api.mock.dto;

import com.example.api.mock.model.order.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderSummaryDto(
        Long id,
        String orderNumber,
        Long customerId,
        OrderStatus status,
        BigDecimal total,
        Instant createdAt
) {}