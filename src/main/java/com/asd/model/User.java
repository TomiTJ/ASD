package com.asd.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Staff user model (planning-only, no JPA yet).
 * When you add JPA later, we’ll annotate with @Entity, @Id, etc.
 */
public class User {

    private Long id;                 // Unique identifier (set by DB later)
    private String fullName;         // "Jane Doe"
    private String email;            // login + contact (unique later)
    private String password;         // store HASH (not plain text) once auth is added
    private Role role;               // ADMIN or READ_ONLY (MVP)
    private Status status;           // ACTIVE or DEACTIVATED
    private LocalDateTime createdAt; // set when user is created
    private LocalDateTime updatedAt; // update on edits

    public User() {
        // default status for new users
        this.status = Status.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public User(Long id, String fullName, String email, String password, Role role, Status status,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status != null ? status : Status.ACTIVE;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
    }

    // Convenience constructor for quick test data
    public User(String fullName, String email, String password, Role role) {
        this(null, fullName, email, password, role, Status.ACTIVE, LocalDateTime.now(), LocalDateTime.now());
    }

    // Getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; touch(); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; touch(); }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; touch(); }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; touch(); }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; touch(); }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    // equals/hashCode: using email if present (treat as unique username in MVP)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(email != null ? email.toLowerCase() : null,
                user.email != null ? user.email.toLowerCase() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email != null ? email.toLowerCase() : null);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
}
