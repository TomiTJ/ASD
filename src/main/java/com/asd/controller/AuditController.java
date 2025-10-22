package com.asd.controller;

import com.asd.dto.AuditDto;
import com.asd.model.Action;
import com.asd.services.AuditService;
import jakarta.servlet.http.HttpSession;
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
    public String viewAuditPage(HttpSession session) {
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
        if (userId == null) {
            return "redirect:/login";
        }
        return "audits";
    }

    @GetMapping("/api/audits")
    @ResponseBody
    public ResponseEntity<List<AuditDto>> getAudits(
            HttpSession session,
            @RequestParam(required = false) Action action,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
        if (userId == null) {
            return ResponseEntity.status(401).body(List.of());
        }
        List<AuditDto> audits = auditService.list(action, from, to);
        return ResponseEntity.ok(audits);
    }
}