package com.asd.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loan_application")
public class LoanApplication {

    public enum Status { SUBMITTED, APPROVED, REJECTED, FEE




















































    } // kept inside to reduce files

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;  // FK to customers.id (no join to keep it simple) uses your Customer entity

    @Column(nullable = false)
    private String product;   // simple text like

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal principal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.SUBMITTED;

    private String notes;
    private LocalDate submittedAt = LocalDate.now();
    private LocalDate decidedAt;

    public LoanApplication() {}

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public BigDecimal getPrincipal() { return principal; }
    public void setPrincipal(BigDecimal principal) { this.principal = principal; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDate getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDate submittedAt) { this.submittedAt = submittedAt; }

    public LocalDate getDecidedAt() { return decidedAt; }
    public void setDecidedAt(LocalDate decidedAt) { this.decidedAt = decidedAt; }
}
