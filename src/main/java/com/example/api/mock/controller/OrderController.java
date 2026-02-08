package com.example.api.mock.controller;

import com.example.api.mock.dto.OrderSummaryDto;
import com.example.api.mock.model.order.OrderStatus;
import com.example.api.mock.service.OrderQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderQueryService service;

    public OrderController(OrderQueryService service) {
        this.service = service;
    }

    @GetMapping
    public Page<OrderSummaryDto> list(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            Pageable pageable
    ) {
        return service.find(customerId, status, from, to, pageable);
    }

    @GetMapping("/{id}")
    public OrderSummaryDto get(@PathVariable Long id) {
        return service.getById(id);
    }
}