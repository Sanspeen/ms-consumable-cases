package com.example.api.mock.dto;

import com.example.api.mock.model.customer.CustomerStatus;

import java.time.Instant;

public record CustomerSummaryDto(
        Long id,
        String email,
        String fullName,
        CustomerStatus status,
        Instant createdAt
) {}