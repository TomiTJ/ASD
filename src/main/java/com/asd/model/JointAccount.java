package com.asd.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "joint_accounts")
public class JointAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "is_joint", nullable = false)
    private Boolean isJoint;

    @Column(name = "linked_date", nullable = false)
    private LocalDate linkedDate;

    @Column(name = "linked_time", nullable = false)
    private LocalTime linkedTime;

    public JointAccount() {
    }

    public JointAccount(Long accountId, Long customerId, Boolean isJoint, LocalDate linkedDate, LocalTime linkedTime) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.isJoint = isJoint;
        this.linkedDate = linkedDate;
        this.linkedTime = linkedTime;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Boolean getIsJoint() {
        return isJoint;
    }

    public void setIsJoint(Boolean isJoint) {
        this.isJoint = isJoint;
    }

    public LocalDate getLinkedDate() {
        return linkedDate;
    }

    public void setLinkedDate(LocalDate linkedDate) {
        this.linkedDate = linkedDate;
    }

    public LocalTime getLinkedTime() {
        return linkedTime;
    }

    public void setLinkedTime(LocalTime linkedTime) {
        this.linkedTime = linkedTime;
    }
}