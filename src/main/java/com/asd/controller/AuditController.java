package com.asd.controller;

import com.asd.dto.AuditDto;
import com.asd.model.Action;
import com.asd.services.AuditService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }


    @GetMapping("/audit")
    public String page(Model model) {
        List<AuditDto> recent = auditService.findAllAudits();
        model.addAttribute("audits", recent);
        return "audit";
    }


    @GetMapping("/api/audits")
    @ResponseBody
    public List<AuditDto> list(
            @RequestParam(required = false) Action action,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return auditService.list(action, from, to);
    }
}
