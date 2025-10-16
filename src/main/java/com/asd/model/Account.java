package com.asd.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="accounts")
public class Account {

    public enum AccountType {
        TRANSACTIONAL,
        SAVINGS,
        CREDIT,
        BUSINESS
    }

    public enum AccountStatus {
        OPEN,
        CLOSED,
        FROZEN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private Long customerId;  // FK to customers table

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Account() {
        this.balance = BigDecimal.ZERO;
        this.accountStatus = AccountStatus.OPEN;
    }

    public Account(String accountNumber, Long customerId, AccountType accountType,
                   AccountStatus accountStatus, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.accountType = accountType;
        this.accountStatus = accountStatus;
        this.balance = balance;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }
        if (this.accountStatus == null) {
            this.accountStatus = AccountStatus.OPEN;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}