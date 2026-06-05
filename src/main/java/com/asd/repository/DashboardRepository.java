package com.asd.repository;

import com.asd.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface DashboardRepository extends JpaRepository<Transaction, UUID> {

    @Query(value = "SELECT COUNT(*) FROM users", nativeQuery = true)
    Long countUsers();

    @Query(value = "SELECT COUNT(*) FROM account", nativeQuery = true)
    long countAccounts();

    @Query(value = "SELECT COUNT(*) FROM transactions", nativeQuery = true)
    long countTransactions();

    // Transaction breakdowns
    @Query(value = "SELECT status, COUNT(*) FROM transactions GROUP BY status", nativeQuery = true)
    List<Object[]> countByTransactionStatus();

    @Query(value = "SELECT type, COUNT(*) FROM transactions GROUP BY type", nativeQuery = true)
    List<Object[]> countByTransactionType();

    // Account breakdowns
    @Query(value = "SELECT account_type, COUNT(*) FROM account GROUP BY account_type", nativeQuery = true)
    List<Object[]> countByAccountType();

    @Query(value = "SELECT account_status, COUNT(*) FROM account GROUP BY account_status", nativeQuery = true)
    List<Object[]> countByAccountStatus();

    // Loan breakdown
    @Query(value = "SELECT status, COUNT(*) FROM loan_application GROUP BY status", nativeQuery = true)
    List<Object[]> countByLoanStatus();

    // Total balance of all open accounts
    @Query(value = "SELECT COALESCE(SUM(balance), 0) FROM account WHERE account_status = 'OPEN'", nativeQuery = true)
    BigDecimal totalOpenBalance();
}