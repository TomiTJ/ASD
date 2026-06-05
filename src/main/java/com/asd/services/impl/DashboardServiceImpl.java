package com.asd.services.impl;

import com.asd.dto.DashboardMetricDto;
import com.asd.repository.DashboardRepository;
import com.asd.services.DashboardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final DashboardRepository repo;

    public DashboardServiceImpl(DashboardRepository repo) {
        this.repo = repo;
    }

    @Override
    public DashboardMetricDto getMetrics() {
        return DashboardMetricDto.builder()
                .totalUsers(repo.countUsers())
                .totalAccounts(repo.countAccounts())
                .totalTransactions(repo.countTransactions())
                .totalOpenBalance(repo.totalOpenBalance())
                .transactionsByStatus(toMap(repo.countByTransactionStatus()))
                .transactionsByType(toMap(repo.countByTransactionType()))
                .accountsByType(toMap(repo.countByAccountType()))
                .accountsByStatus(toMap(repo.countByAccountStatus()))
                .loansByStatus(toMap(repo.countByLoanStatus()))
                .build();
    }

    private Map<String, Long> toMap(List<Object[]> rows) {
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : rows) {
            String key = row[0] != null ? row[0].toString() : "UNKNOWN";
            long val = ((Number) row[1]).longValue();
            map.put(key, val);
        }
        return map;
    }
}
