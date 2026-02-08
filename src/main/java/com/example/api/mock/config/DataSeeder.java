package com.example.api.mock.config;

import com.example.api.mock.model.customer.Customer;
import com.example.api.mock.model.customer.CustomerStatus;
import com.example.api.mock.model.order.Order;
import com.example.api.mock.model.order.OrderItem;
import com.example.api.mock.model.order.OrderStatus;
import com.example.api.mock.model.product.Product;
import com.example.api.mock.repository.CustomerRepository;
import com.example.api.mock.repository.OrderRepository;
import com.example.api.mock.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(CustomerRepository customers, ProductRepository products, OrderRepository orders) {
        return args -> {
            if (customers.count() > 0) return;

            // Customers
            var c1 = new Customer(); c1.setEmail("ana@example.com"); c1.setFullName("Ana Gómez"); c1.setStatus(CustomerStatus.ACTIVE);
            var c2 = new Customer(); c2.setEmail("luis@example.com"); c2.setFullName("Luis Pérez"); c2.setStatus(CustomerStatus.ACTIVE);
            var c3 = new Customer(); c3.setEmail("sofia@example.com"); c3.setFullName("Sofía Rodríguez"); c3.setStatus(CustomerStatus.SUSPENDED);
            customers.saveAll(List.of(c1, c2, c3));

            // Products
            var p1 = new Product(); p1.setSku("SKU-1001"); p1.setName("Mechanical Keyboard"); p1.setPrice(new BigDecimal("399000.00")); p1.setActive(true);
            var p2 = new Product(); p2.setSku("SKU-1002"); p2.setName("Noise Cancelling Headphones"); p2.setPrice(new BigDecimal("549000.00")); p2.setActive(true);
            var p3 = new Product(); p3.setSku("SKU-1003"); p3.setName("USB-C Hub"); p3.setPrice(new BigDecimal("129000.00")); p3.setActive(true);
            var p4 = new Product(); p4.setSku("SKU-1004"); p4.setName("Old Stock Mouse"); p4.setPrice(new BigDecimal("59000.00")); p4.setActive(false);
            products.saveAll(List.of(p1, p2, p3, p4));

            // Orders + Items (con totales)
            var rnd = new Random(42);

            createOrder(orders, c1, "ORD-0001", OrderStatus.CREATED, List.of(
                    item(p1, 1), item(p3, 2)
            ));

            createOrder(orders, c1, "ORD-0002", OrderStatus.PAID, List.of(
                    item(p2, 1)
            ));

            createOrder(orders, c2, "ORD-0003", OrderStatus.CANCELLED, List.of(
                    item(p3, 1), item(p4, 1)
            ));
        };
    }

    private static OrderItem item(Product p, int qty) {
        var it = new OrderItem();
        it.setProduct(p);
        it.setQuantity(qty);
        it.setUnitPrice(p.getPrice());
        // lineTotal lo calcula @PrePersist/@PreUpdate
        return it;
    }

    private static void createOrder(OrderRepository orders, Customer c, String orderNumber, OrderStatus status, List<OrderItem> items) {
        var o = new Order();
        o.setCustomer(c);
        o.setOrderNumber(orderNumber);
        o.setStatus(status);

        for (var it : items) {
            it.setOrder(o);
            o.getItems().add(it);
        }

        // calcular total
        o.recalcTotals();
        orders.save(o);
    }
}