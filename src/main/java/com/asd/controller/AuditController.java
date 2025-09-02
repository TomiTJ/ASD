package com.asd.controller;

import com.asd.model.Audit;
import com.asd.repository.AuditRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuditController {

    private final AuditRepository auditRepository;

    public AuditController(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @GetMapping("/audit")
    public String Audit(Model model) {
        model.addAttribute("audit", new Audit());
        return "Audit";
    }
}
