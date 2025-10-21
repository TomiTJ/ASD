package com.asd.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String full_name;
    private String email;

    public Customer() {}

    public Customer(Long id, String full_name, String email) {
        this.id = id;
        this.full_name = full_name;
        this.email = email;
    }
}
