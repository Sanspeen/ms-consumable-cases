package com.example.api.mock.repository;

import com.example.api.mock.model.order.Order;
import com.example.api.mock.model.order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCustomerId(Long customerId, Pageable pageable);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findByCustomerIdAndStatus(Long customerId, OrderStatus status, Pageable pageable);

    Page<Order> findByCreatedAtBetween(Instant from, Instant to, Pageable pageable);

    Page<Order> findByCustomerIdAndCreatedAtBetween(Long customerId, Instant from, Instant to, Pageable pageable);
}