package com.asd.controller;

import com.asd.dto.DashboardMetricDto;
import com.asd.services.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Aggregated metrics for the admin dashboard")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/metrics")
    @Operation(summary = "Get dashboard metrics",
               description = "Returns total counts and breakdowns for users, accounts, transactions, loans, and total AUM")
    public DashboardMetricDto metrics() {
        return service.getMetrics();
    }
}