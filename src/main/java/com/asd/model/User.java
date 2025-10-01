package com.asd.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="full_name")
    private String full_name;
    @Column(name="email",nullable=false)
    private String email;
    @Column(name="password", nullable = false)
    private String password;
    @Column(name="role")
    private String role;
    @Column(name="status")
    private String status;
    @Column(name="created_at")
    private LocalDateTime created_at;
    @Column(name="updated_at")
    private LocalDateTime updated_at;

  }
