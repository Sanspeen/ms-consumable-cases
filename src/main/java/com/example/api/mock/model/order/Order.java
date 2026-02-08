package com.example.api.mock.model.order;


import com.example.api.mock.model.customer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_orders_order_number", columnList = "orderNumber", unique = true)
})
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String orderNumber;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.CREATED;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public void recalcTotals() {
        this.total = items.stream()
                .map(it -> {
                    var price = it.getUnitPrice() == null ? BigDecimal.ZERO : it.getUnitPrice();
                    return price.multiply(BigDecimal.valueOf(it.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}