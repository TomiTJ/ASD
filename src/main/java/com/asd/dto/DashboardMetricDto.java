package com.asd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardMetricDto {
    private long totalUsers;
    private long totalAccounts;
    private long totalTransactions;
    private BigDecimal totalOpenBalance;

    // Breakdowns for charts
    private Map<String, Long> transactionsByStatus;
    private Map<String, Long> transactionsByType;
    private Map<String, Long> accountsByType;
    private Map<String, Long> accountsByStatus;
    private Map<String, Long> loansByStatus;
}

