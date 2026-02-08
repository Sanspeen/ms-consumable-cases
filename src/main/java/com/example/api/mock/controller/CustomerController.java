package com.example.api.mock.controller;

import com.example.api.mock.dto.CustomerSummaryDto;
import com.example.api.mock.model.customer.CustomerStatus;
import com.example.api.mock.service.CustomerQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerQueryService service;

    public CustomerController(CustomerQueryService service) {
        this.service = service;
    }

    @GetMapping
    public Page<CustomerSummaryDto> list(
            @RequestParam(required = false) CustomerStatus status,
            @RequestParam(required = false) String q,
            Pageable pageable
    ) {
        return service.find(status, q, pageable);
    }

    @GetMapping("/{id}")
    public CustomerSummaryDto get(@PathVariable Long id) {
        return service.getById(id);
    }
}