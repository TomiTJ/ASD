package com.asd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

enum accountStatus {
    TRANSACTIONAL,
    SAVINGS,
    CREDIT,
    BUSINESS
}

enum accountType {
    OPEN,
    CLOSED,
    FROZEN
}

@Data
@Entity
@Table(name="account")
public class Account {

    @Id
    private Long id;
    private int accountNumber;
    private int userId;
    private accountStatus accountStatus;
    private accountType accountType;
    private double balance;

    public Account (int accountNumber, int userID, accountStatus accountStatus, accountType accountType, double balance) {
        this.accountNumber = accountNumber;
        this.userId = userID;
        this.accountStatus = accountStatus;
        this.accountType = accountType;
        this.balance = balance;
    }


    public Account() {

    }
}
