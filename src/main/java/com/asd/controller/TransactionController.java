package com.asd.controller;


import com.asd.model.Transaction;
import com.asd.repository.TransactionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TransactionController {

    private final TransactionRepository transactionRepo;

    public TransactionController(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    @GetMapping("/transactions")
    public String showTransactions(Model model) {
        List<Transaction> transactions = transactionRepo.findAll();
        model.addAttribute("transactions", transactions);
        return "transactionsPage";
    };
}
