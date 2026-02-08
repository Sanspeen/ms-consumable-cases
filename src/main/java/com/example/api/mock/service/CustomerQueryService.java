package com.example.api.mock.service;

import com.example.api.mock.dto.CustomerSummaryDto;
import com.example.api.mock.model.customer.Customer;
import com.example.api.mock.model.customer.CustomerStatus;
import com.example.api.mock.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerQueryService {

    private final CustomerRepository repo;

    public CustomerQueryService(CustomerRepository repo) {
        this.repo = repo;
    }

    public Page<CustomerSummaryDto> find(CustomerStatus status, String q, Pageable pageable) {
        Page<Customer> page;

        boolean hasStatus = status != null;
        boolean hasQ = q != null && !q.isBlank();

        if (hasStatus && hasQ) {
            String qq = q.trim();
            page = repo.findByStatusAndFullNameContainingIgnoreCaseOrStatusAndEmailContainingIgnoreCase(
                    status, qq, status, qq, pageable
            );
        } else if (hasStatus) {
            page = repo.findByStatus(status, pageable);
        } else if (hasQ) {
            String qq = q.trim();
            page = repo.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(qq, qq, pageable);
        } else {
            page = repo.findAll(pageable);
        }

        return page.map(this::toDto);
    }

    public CustomerSummaryDto getById(Long id) {
        Customer c = repo.findById(id).orElseThrow(); // por ahora; luego ponemos 404 bonito
        return toDto(c);
    }

    private CustomerSummaryDto toDto(Customer c) {
        return new CustomerSummaryDto(c.getId(), c.getEmail(), c.getFullName(), c.getStatus(), c.getCreatedAt());
    }
}