package com.asd.model;

import jakarta.persistence.*;

/*** Notes:
    - Transaction Attributes TBD:
        * Account (ID)
        * Timestamp (date & time)
***/
enum transactionType {
    WITHDRAWAL,
    DEPOSIT,
    TRANSFER
}

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
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
