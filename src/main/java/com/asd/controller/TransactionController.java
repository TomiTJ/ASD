package com.asd.controller;


import com.asd.dto.TransactionDto;
import com.asd.model.Transaction;
import com.asd.model.User;
import com.asd.repository.TransactionRepository;
import com.asd.services.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.List;

@Controller
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }


    @GetMapping("/transactions")
    public String showTransactions(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "") String type,
            @RequestParam(required = false, defaultValue = "") String status,
            HttpSession session,
            Model model) {
        Object userId = session.getAttribute("userId");
        Object userName = session.getAttribute("userName");
        Object userRole = session.getAttribute("userRole");
        if (userId == null) {
            return "redirect:/login";
        }
        if (search != null || type != null || status != null) {
            List<TransactionDto> transactions = transactionService.findFilteredTransactions(search, type, status);
            model.addAttribute("transactions", transactions);
        }
        else {
            List<TransactionDto> transactions = transactionService.findallTransactions();
            model.addAttribute("transactions", transactions);
        }
        model.addAttribute("userName",userName );
        model.addAttribute("userRole",userRole );
        return "transactionsPage";
    }

    @GetMapping("/transactions/download")
    public String handleDownloadRequest(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "") String type,
            @RequestParam(required = false, defaultValue = "") String status,
            RedirectAttributes redirectAttributes) throws IOException {
        List<TransactionDto> transactions = transactionService.findFilteredTransactions(search, type, status);

        //If transactions is empty throw error message.
        if (transactions.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No transactions found");
            return "redirect:/transactions";
        }
        else {
            return "redirect:/transactions/download/file"
                    + "?search=" + (search != null ? search : "")
                    + "&type=" + (type != null ? type : "")
                    + "&status=" + (status != null ? status : "");
        }
    }

    @GetMapping("/transactions/download/file")
    public ResponseEntity<Resource> downloadCSV(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) throws IOException {
        List<TransactionDto> transactions = transactionService.findFilteredTransactions(search, type, status);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(outputStream), CSVFormat.DEFAULT);
        csvPrinter.printRecord("Transaction ID", "Customer", "Type", "Amount", "Status", "Time Created", "Date Created");

        for (TransactionDto transaction : transactions) {
            csvPrinter.printRecord(
                    transaction.getId(),
                    sanitizeCsv(transaction.getCustomer().getName()),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getStatus(),
                    sanitizeCsv(transaction.getTimeCreatedAt()),
                    sanitizeCsv(transaction.getDateCreatedAt()));
        }
        csvPrinter.flush();

        InputStreamResource data = new InputStreamResource(
                new ByteArrayInputStream(outputStream.toByteArray())
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(data);
    }

    /**
     * Prevents CSV injection: values starting with formula-trigger characters
     * are prefixed with a single quote so spreadsheet apps treat them as text.
     */
    private String sanitizeCsv(String value) {
        if (value == null) return "";
        String trimmed = value.trim();
        if (!trimmed.isEmpty() && "=+-@\t\r".indexOf(trimmed.charAt(0)) >= 0) {
            return "'" + trimmed;
        }
        return trimmed;
    }
}
