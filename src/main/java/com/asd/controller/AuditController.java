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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
            @RequestParam(required = false) String action, // take as String, parse manually
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        Integer userId = (session != null) ? (Integer) session.getAttribute("userId") : null;
        if (userId == null) return ResponseEntity.status(401).body(List.of());

        Action actionEnum = null;
        if (action != null && !action.isBlank()) {
            try { actionEnum = Action.valueOf(action.trim().toUpperCase()); } catch (IllegalArgumentException ignored) {}
        }

        ZoneId zone = ZoneOffset.UTC;  // use UTC — consistent regardless of where the server runs
        Instant fromTs = (from != null) ? from.atStartOfDay(zone).toInstant() : null;
        Instant toTs   = (to   != null) ? to.plusDays(1).atStartOfDay(zone).minusNanos(1).toInstant() : null;

        List<AuditDto> audits = auditService.list(actionEnum, fromTs, toTs);
        return ResponseEntity.ok(audits);
    }
}