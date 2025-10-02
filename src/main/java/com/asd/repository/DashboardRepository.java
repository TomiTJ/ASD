package com.asd.repository;

import com.asd.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DashboardRepository extends JpaRepository<Transaction, UUID> {

    @Query(value = "SELECT COUNT(*) FROM users", nativeQuery = true)
    public Long countUsers();

    @Query(value = "SELECT COUNT(*) FROM customers", nativeQuery = true)
    public long countAccounts();

    @Query(value = "SELECT COUNT(*) FROM transactions", nativeQuery = true)
    public long countTransactions();


}