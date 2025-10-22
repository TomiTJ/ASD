package com.asd.dto;

import com.asd.model.Account;
import com.asd.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String accountNumber;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private Account.AccountType accountType;
    private Account.AccountStatus accountStatus;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}