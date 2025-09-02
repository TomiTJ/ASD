package com.asd.model;

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

public class Account {

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

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public accountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(accountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public accountType getAccountType() {
        return accountType;
    }

    public void setAccountType(accountType accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
