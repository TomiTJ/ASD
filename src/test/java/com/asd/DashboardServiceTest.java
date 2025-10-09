package com.asd;

import com.asd.dto.DashboardMetricDto;
import com.asd.services.DashboardService;
import com.asd.repository.AccountRepository;
import com.asd.repository.TransactionRepository;
import com.asd.services.impl.DashboardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;



public class DashboardServiceTest {

    private DashboardService dashboardService;
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository = Mockito.mock(TransactionRepository.class);

        dashboardService = new DashboardService() {
            @Override
            public DashboardMetricDto getMetrics() {
                return null;
            }
        };

    }

    @Test
    void testGetDashboard() {
        when(transactionRepository.count()).thenReturn(6L);

        var metrics = dashboardService.getMetrics();

    }

}
