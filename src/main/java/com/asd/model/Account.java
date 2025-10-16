package com.asd.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "account")
public class Account {

    /** WHAT the account is (product/type): e.g., SAVINGS, CREDIT, BUSINESS, TRANSACTIONAL */
    public enum AccountType {
        TRANSACTIONAL,
        SAVINGS,
        CREDIT,
        BUSINESS
    }

    /** Lifecycle/status: e.g., OPEN, FROZEN, CLOSED */
    public enum AccountStatus {
        OPEN,
        FROZEN,
        CLOSED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Human/accounting number; keep int if that’s how your DB is defined */
    @Column(nullable = false, unique = true)
    private int accountNumber;

    /** Foreign key to users.id (simple scalar; or change to @ManyToOne<User> if you want) */
    @Column(nullable = false)
    private int userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountStatus accountStatus;

    @Column(nullable = false)
    private double balance;

    public Account() {}

    public Account(int accountNumber, int userId,
                   AccountType accountType, AccountStatus accountStatus, double balance) {
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.accountType = accountType;
        this.accountStatus = accountStatus;
        this.balance = balance;
    }
}
