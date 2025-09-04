package com.asd.controller;

import com.asd.model.LoanApplication;
import com.asd.repository.LoanApplicationRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loan-applications")
public class LoanApplicationController {

    private final LoanApplicationRepository repo;

    public LoanApplicationController(LoanApplicationRepository repo) {
        this.repo = repo;
    }

    // Minimal inline DTOs to keep file count down:
    public static record CreateRequest(Long customerId, String product, BigDecimal principal) {}
    public static record DecisionRequest(boolean approve, String notes) {}

    // CUSTOMER: submit an application
    @PostMapping
    public LoanApplication submit(@RequestBody CreateRequest req) {
        LoanApplication a = new LoanApplication();
        a.setCustomerId(req.customerId());
        a.setProduct(req.product());
        a.setPrincipal(req.principal());
        a.setStatus(LoanApplication.Status.SUBMITTED);
        a.setSubmittedAt(LocalDate.now());
        return repo.save(a);
    }

    // ADMIN: list applications (optionally filter by status=SUBMITTED|APPROVED|REJECTED)
    @GetMapping
    public List<LoanApplication> list(@RequestParam(required = false) String status) {
        if (status == null || status.isBlank()) return repo.findAll();
        return repo.findByStatus(LoanApplication.Status.valueOf(status));
    }

    // ADMIN: approve/reject
    @PostMapping("/{id}/decision")
    public LoanApplication decide(@PathVariable Long id, @RequestBody DecisionRequest req) {
        LoanApplication a = repo.findById(id).orElseThrow();
        a.setNotes(req.notes());
        a.setDecidedAt(LocalDate.now());
        a.setStatus(req.approve() ? LoanApplication.Status.APPROVED : LoanApplication.Status.REJECTED);
        return repo.save(a);
    }
}
