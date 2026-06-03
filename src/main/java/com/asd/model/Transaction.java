package com.asd.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

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
    @Column(precision = 12, scale = 2)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private OffsetDateTime createdAt;

    public Transaction() {}

    public Transaction(Customer customer, TransactionType type, BigDecimal amount, TransactionStatus status, OffsetDateTime createdAt) {
        this.customer = customer;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public Customer getCustomer() {
        return customer;
    }
    public TransactionType getType() { return type; }
    public TransactionStatus getStatus() { return status; }
    public OffsetDateTime getCreatedAt() { return createdAt; }


}
