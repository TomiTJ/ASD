package com.asd.controller;

import com.asd.model.Audit;
import com.asd.repository.AuditRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AuditController {

    private final AuditRepository auditRepository;

    public AuditController(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    /*@GetMapping("/audit")
    public String Audit(Model model) {
        List<Audit> audits = auditRepository.findAll();
        model.addAttribute("audits", audits);
        return "audit";
    } */
    //found error
    @RequestMapping("/audit")
    public String start(Model model) {
        return "audit";
    }
}
