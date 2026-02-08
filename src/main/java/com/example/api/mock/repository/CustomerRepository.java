package com.example.api.mock.repository;


import com.example.api.mock.model.customer.Customer;
import com.example.api.mock.model.customer.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Page<Customer> findByStatus(CustomerStatus status, Pageable pageable);

    Page<Customer> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String nameQ, String emailQ, Pageable pageable
    );

    Page<Customer> findByStatusAndFullNameContainingIgnoreCaseOrStatusAndEmailContainingIgnoreCase(
            CustomerStatus s1, String nameQ, CustomerStatus s2, String emailQ, Pageable pageable
    );
}