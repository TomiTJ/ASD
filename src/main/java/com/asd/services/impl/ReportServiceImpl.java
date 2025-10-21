package com.asd.services.impl;

import com.asd.dto.TransactionDto;
import com.asd.repository.AccountRepository;
import com.asd.repository.TransactionRepository;
import com.asd.services.ReportService;
import com.asd.services.TransactionService;
import com.asd.util.ExportUtil;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
        return generateReportFiltered(type,format,null,null);
    }

    public byte[] generateReportFiltered(String type, String format, String start, String end) throws IOException {
        List<String> headers;
        List<List<String>> data;

        switch (type.toLowerCase()) {
            case "transaction" -> {
                List<TransactionDto> transactions = transactionService.findallTransactions();
                List<TransactionDto> filtered = filterByDate(transactions,start,end);

                headers = List.of("Transaction ID", "Customer", "Type", "Amount", "Status", "Time Created");
                data = filtered.stream()
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

    private List<TransactionDto> filterByDate(List<TransactionDto> transactions, String start, String end) {

        final ZoneOffset offset = OffsetDateTime.now().getOffset();

        OffsetDateTime startDate;
        OffsetDateTime endDate;

        if (start != null && !start.isBlank()) {
            LocalDate startLocalDate = LocalDate.parse(start);
            startDate = startLocalDate.atStartOfDay().atOffset(offset);
        } else {
            startDate = null;

        }
        if (end != null && !end.isBlank()) {
            LocalDate endLocalDate = LocalDate.parse(end);
            endDate = endLocalDate.atTime(23, 59, 59).atOffset(offset);
        } else {
            endDate = null;
        }

        return transactions.stream()
                .filter(t -> {
                    OffsetDateTime created = t.getCreatedAt();
                    if (created == null) return false;
                    boolean afterStart = (startDate == null || !created.isBefore(startDate));
                    boolean beforeEnd = (endDate == null || !created.isAfter(endDate));
                    return afterStart && beforeEnd;
                })
                .toList();
    }

}
