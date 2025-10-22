package com.asd.controller;

import com.asd.dto.TransactionDto;
import com.asd.services.ReportService;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/reports")
    public String showTransactions(HttpSession session, Model model) {
        Object userId = session.getAttribute("userId");
        Object userName = session.getAttribute("userName");
        Object userRole = session.getAttribute("userRole");
        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("userName",userName );
        model.addAttribute("userRole",userRole );
        return "reports";
    }

    @GetMapping("/reports/download")
    public ResponseEntity<byte[]> downloadReport(
            @RequestParam String type,
            @RequestParam String format,
            @RequestParam String start,
            @RequestParam String end) throws IOException {

        byte[] file = reportService.generateReportFiltered(type, format, start, end);

        String filename = type + "_report." + switch (format.toLowerCase()) {
            case "excel" -> "xlsx";
            case "csv" -> "csv";
            case "pdf" -> "pdf";
            default -> "dat";
        };

        String contentType = switch (format.toLowerCase()) {
            case "excel" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "csv" -> "text/csv";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }

}
