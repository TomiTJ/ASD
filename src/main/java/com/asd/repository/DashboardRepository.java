package com.asd.repository;

import com.asd.dto.TransactionTrendDto;
import com.asd.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DashboardRepository extends JpaRepository<Transaction, UUID> {

    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();

    @Query("SELECT COUNT(a) FROM Account a")
    long countAccounts();

    @Query("SELECT COUNT(t) FROM Transaction t")
    long countTransactions();


}