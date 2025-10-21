package com.asd.controller;

import com.asd.dto.DashboardMetricDto;
import com.asd.services.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/metrics")
    public DashboardMetricDto metrics() {
        return service.getMetrics();
    }

    @GetMapping("/auditmetrics")
    public DashboardMetricDto auditmetrics() {
        return service.getAuditMetrics();
    }
}