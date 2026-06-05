package com.asd.controller;

import com.asd.model.LoanApplication;
import com.asd.repository.LoanApplicationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loan-applications")
@Tag(name = "Loan Applications", description = "Submit and review loan applications")
public class LoanApplicationController {

    private final LoanApplicationRepository repo;

    public LoanApplicationController(LoanApplicationRepository repo) {
        this.repo = repo;
    }

    // Minimal inline DTOs to keep file count down:
    public static record CreateRequest(Long customerId, String product, BigDecimal principal) {}
    public static record DecisionRequest(boolean approve, String notes) {}

    @PostMapping
    @Operation(summary = "Submit a loan application",
               description = "Creates a new loan application with SUBMITTED status")
    public LoanApplication submit(@RequestBody CreateRequest req) {
        LoanApplication a = new LoanApplication();
        a.setCustomerId(req.customerId());
        a.setProduct(req.product());
        a.setPrincipal(req.principal());
        a.setStatus(LoanApplication.Status.SUBMITTED);
        a.setSubmittedAt(LocalDate.now());
        return repo.save(a);
    }

    @GetMapping
    @Operation(summary = "List loan applications",
               description = "Returns all applications. Filter by status: SUBMITTED, APPROVED, REJECTED")
    public List<LoanApplication> list(
            @Parameter(description = "Filter by status: SUBMITTED | APPROVED | REJECTED")
            @RequestParam(required = false) String status) {
        if (status == null || status.isBlank()) return repo.findAll();
        return repo.findByStatus(LoanApplication.Status.valueOf(status));
    }

    @PostMapping("/{id}/decision")
    @Operation(summary = "Approve or reject a loan application",
               description = "Set approve=true to approve, false to reject. Include optional notes.")
    public LoanApplication decide(
            @Parameter(description = "Loan application ID") @PathVariable Long id,
            @RequestBody DecisionRequest req) {
        LoanApplication a = repo.findById(id).orElseThrow();
        a.setNotes(req.notes());
        a.setDecidedAt(LocalDate.now());
        a.setStatus(req.approve() ? LoanApplication.Status.APPROVED : LoanApplication.Status.REJECTED);
        return repo.save(a);
    }
}
