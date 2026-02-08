package com.example.api.mock.service;


import com.example.api.mock.dto.OrderSummaryDto;
import com.example.api.mock.model.order.Order;
import com.example.api.mock.model.order.OrderStatus;
import com.example.api.mock.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderQueryService {

    private final OrderRepository repo;

    public OrderQueryService(OrderRepository repo) {
        this.repo = repo;
    }

    public Page<OrderSummaryDto> find(Long customerId, OrderStatus status, Instant from, Instant to, Pageable pageable) {
        Page<Order> page;

        boolean hasCustomer = customerId != null;
        boolean hasStatus = status != null;
        boolean hasDates = from != null && to != null;

        if (hasCustomer && hasStatus) {
            page = repo.findByCustomerIdAndStatus(customerId, status, pageable);
        } else if (hasCustomer && hasDates) {
            page = repo.findByCustomerIdAndCreatedAtBetween(customerId, from, to, pageable);
        } else if (hasCustomer) {
            page = repo.findByCustomerId(customerId, pageable);
        } else if (hasStatus) {
            page = repo.findByStatus(status, pageable);
        } else if (hasDates) {
            page = repo.findByCreatedAtBetween(from, to, pageable);
        } else {
            page = repo.findAll(pageable);
        }

        return page.map(this::toDto);
    }

    public OrderSummaryDto getById(Long id) {
        Order o = repo.findById(id).orElseThrow();
        return toDto(o);
    }

    private OrderSummaryDto toDto(Order o) {
        return new OrderSummaryDto(
                o.getId(),
                o.getOrderNumber(),
                o.getCustomer().getId(),
                o.getStatus(),
                o.getTotal(),
                o.getCreatedAt()
        );
    }
}
