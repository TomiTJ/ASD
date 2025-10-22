package com.asd.repository;

import com.asd.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Search by account number (case-insensitive)
    List<Account> findByAccountNumberContainingIgnoreCase(String accountNumber);

    // Find accounts by customer IDs
    List<Account> findByCustomerIdIn(List<Long> customerIds);

}