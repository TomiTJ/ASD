package com.asd.services.impl;

import ch.qos.logback.classic.spi.LoggingEventVO;
import com.asd.dto.DashboardMetricDto;
import com.asd.repository.DashboardRepository;
import com.asd.services.DashboardService;
import org.springframework.stereotype.Service;

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


    @Override
    public DashboardMetricDto getAuditMetrics() {

        return DashboardMetricDto.builder()
                .totalActions(repo.countActions()).
                build();

    }


}
