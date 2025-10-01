package com.asd.services.impl;

import com.asd.dto.DashboardMetricDto;
import com.asd.dto.TransactionTrendDto;
import com.asd.repository.DashboardRepository;
import com.asd.services.DashboardService;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .build();
    }


}
