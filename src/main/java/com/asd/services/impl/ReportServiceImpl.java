package com.asd.services.impl;

import com.asd.dto.TransactionDto;
import com.asd.repository.AccountRepository;
import com.asd.repository.TransactionRepository;
import com.asd.services.ReportService;
import com.asd.services.TransactionService;
import com.asd.util.ExportUtil;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final TransactionService transactionService;
    private final AccountRepository accountRepository;

    public ReportServiceImpl(TransactionRepository transactionRepository, TransactionService transactionService, AccountRepository accountRepository) {
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
    }

    public byte[] generateReport(String type, String format) throws IOException {
        List<String> headers;
        List<List<String>> data;

        switch (type.toLowerCase()) {
            case "transaction" -> {
                List<TransactionDto> transactions = transactionService.findallTransactions();

                headers = List.of("Transaction ID", "Customer", "Type", "Amount", "Status", "Time Created");
                data = transactions.stream()
                        .map(t -> List.of(
                                String.valueOf(t.getId()),
                                String.valueOf(t.getCustomer().getFull_name()),
                                String.valueOf(t.getType()),
                                String.format("$%.2f", t.getAmount()),
                                String.valueOf(t.getStatus()),
                                String.valueOf(t.getCreatedAt())
                        ))
                        .toList();

                return switch (format.toLowerCase()) {
                    case "excel" -> ExportUtil.toExcel(headers, data);
                    case "csv" -> ExportUtil.toCSV(headers, data);
                    case "pdf" -> ExportUtil.toPDF(headers, data);
                    default -> throw new IllegalArgumentException("Unsupported format: " + format);
                };
            }
            default -> throw new IllegalArgumentException("Unsupported report type: " + type);
        }
    }
}
