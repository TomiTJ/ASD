package com.asd.model;

import com.asd.services.TransactionService;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "transactions")
public class Transaction {

    public enum TransactionType {
        WITHDRAWAL,
        DEPOSIT,
        TRANSFER
    }

    public enum TransactionStatus {
        PENDING,
        FAILED,
        COMPLETED,
        FLAGGED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private OffsetDateTime createdAt;

    public Transaction() {}

    public Transaction(Customer customer, TransactionType type, Double amount, TransactionStatus status, OffsetDateTime createdAt) {
        this.customer = customer;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }
    public double getAmount() {
        return amount;
    }
    public Customer getCustomer() {
        return customer;
    }
    public TransactionType getType() { return type; }
    public TransactionStatus getStatus() { return status; }
    public OffsetDateTime getCreatedAt() { return createdAt; }


}
