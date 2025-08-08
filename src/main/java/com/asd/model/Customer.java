package com.asd.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Fetch;

import java.util.List;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "customer", cascade =  CascadeType.ALL)
    private List<Account> accounts ;


}
