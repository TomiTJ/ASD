package com.asd.dto;

import com.asd.model.Account;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountDetail {

    private Long id;
    private String accountNumber;
    private String accountType;
    private String accountStatus;
    private BigDecimal balance;
    private String primaryCustomerName;
    private boolean joint;
    private List<JointCustomerDto> jointCustomers;
    private List<CustomerSimpleDto> availableCustomers;

    public AccountDetail() {
        this.jointCustomers = new ArrayList<>();
        this.availableCustomers = new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getPrimaryCustomerName() {
        return primaryCustomerName;
    }

    public void setPrimaryCustomerName(String primaryCustomerName) {
        this.primaryCustomerName = primaryCustomerName;
    }

    public boolean isJoint() {
        return joint;
    }

    public void setJoint(boolean joint) {
        this.joint = joint;
    }

    public List<JointCustomerDto> getJointCustomers() {
        return jointCustomers;
    }

    public void setJointCustomers(List<JointCustomerDto> jointCustomers) {
        this.jointCustomers = jointCustomers;
    }

    public List<CustomerSimpleDto> getAvailableCustomers() {
        return availableCustomers;
    }

    public void setAvailableCustomers(List<CustomerSimpleDto> availableCustomers) {
        this.availableCustomers = availableCustomers;
    }

    // Inner class for joint customer info
    public static class JointCustomerDto {
        private String customerId;
        private String customerName;
        private String linkedDate;
        private String linkedTime;

        public JointCustomerDto() {
        }

        public JointCustomerDto(String customerId, String customerName, String linkedDate, String linkedTime) {
            this.customerId = customerId;
            this.customerName = customerName;
            this.linkedDate = linkedDate;
            this.linkedTime = linkedTime;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getLinkedDate() {
            return linkedDate;
        }

        public void setLinkedDate(String linkedDate) {
            this.linkedDate = linkedDate;
        }

        public String getLinkedTime() {
            return linkedTime;
        }

        public void setLinkedTime(String linkedTime) {
            this.linkedTime = linkedTime;
        }
    }

    // Inner class for available customers
    public static class CustomerSimpleDto {
        private String id;
        private String name;
        private String email;

        public CustomerSimpleDto() {
        }

        public CustomerSimpleDto(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}