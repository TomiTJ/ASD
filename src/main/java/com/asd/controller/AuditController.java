package com.asd.controller;

import com.asd.dto.AuditDto;
import com.asd.model.Action;
import com.asd.services.AuditService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/audit")
    public String viewAuditPage() {
        return "audit"; // Loads audit.html
    }

    @GetMapping("/api/audits")
    @ResponseBody
    public ResponseEntity<List<AuditDto>> getAudits(
            @RequestParam(required = false) Action action,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        List<AuditDto> audits = auditService.list(action, from, to);
        return ResponseEntity.ok(audits);
    }
}