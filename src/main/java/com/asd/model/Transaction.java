package com.asd.model;

import jakarta.persistence.*;

/*** Notes:
    - Transaction Attributes:
        * Transaction ID (001, 002, 003,etc)
        * Customer (Entity/Class or ID)
        * Account (ID)
        * Type (Withdrawal, Deposit, Transfer)
        * Amount ($ double)
        * Timestamp (date & time)
***/

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Transaction() {}

    public Transaction(Customer customer, Double amount) {
        this.customer = customer;
        this.amount = amount;

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
}
