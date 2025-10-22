package com.asd.services.impl;

import com.asd.dto.AccountDto;
import com.asd.dto.TransactionDto;
import com.asd.model.Account;
import com.asd.repository.AccountRepository;
import com.asd.repository.TransactionRepository;
import com.asd.services.AccountService;
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
    private final AccountService accountService;

    public ReportServiceImpl(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
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
                                String.valueOf(t.getCustomer().getName()),
                                String.valueOf(t.getType()),
                                String.format("$%.2f", t.getAmount()),
                                String.valueOf(t.getStatus()),
                                String.valueOf(t.getCreatedAt())
                        ))
                        .toList();

                return export(format,headers,data);
            }
            case "account" -> {
                List<AccountDto> accounts = accountService.findAllAccounts();
                List<AccountDto> filtered = filterAccountsByDate(accounts, start,end);
                headers = List.of("Account ID", "Account Number", "Customer ID", "Account Type", "Account Status", "Balance", "Created At", "Updated At");
                data = filtered.stream()
                        .map(a -> List.of(
                                String.valueOf(a.getId()),
                                String.valueOf(a.getAccountNumber()),
                                String.valueOf(a.getCustomerId()),
                                String.valueOf(a.getAccountType()),
                                String.valueOf(a.getAccountStatus()),
                                String.format("$%.2f", a.getBalance()),
                                String.valueOf(a.getCreatedAt()),
                                String.valueOf(a.getUpdatedAt())
                        ))
                        .toList();
                return export(format,headers,data);
            }
            default -> throw new IllegalArgumentException("Unsupported report type: " + type);
        }
    }

    private byte[] export(String format, List<String> headers, List<List<String>> data) throws IOException {
        return switch (format.toLowerCase()) {
            case "excel" -> ExportUtil.toExcel(headers, data);
            case "csv" -> ExportUtil.toCSV(headers, data);
            case "pdf" -> ExportUtil.toPDF(headers, data);
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };
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
                    OffsetDateTime created = t.getCreatedAt(); //filters if getCreatedAt() is between afterStart and beforeEnd
                    if (created == null) return false;
                    boolean afterStart = (startDate == null || !created.isBefore(startDate));
                    boolean beforeEnd = (endDate == null || !created.isAfter(endDate));
                    return afterStart && beforeEnd;
                })
                .toList();
    }
    private List<AccountDto> filterAccountsByDate(List<AccountDto> accounts, String start, String end) {

        LocalDateTime startDate;
        LocalDateTime endDate;

        if (start != null && !start.isBlank()) {
            LocalDate startLocalDate = LocalDate.parse(start);
            startDate = startLocalDate.atStartOfDay();
        } else {
            startDate = null;
        }

        if (end != null && !end.isBlank()) {
            LocalDate endLocalDate = LocalDate.parse(end);
            endDate = endLocalDate.atTime(23, 59, 59);
        } else {
            endDate = null;
        }

        return accounts.stream()
                .filter(t -> {
                    LocalDateTime created = t.getCreatedAt();
                    if (created == null) return false;

                    boolean afterStart = (startDate == null || !created.isBefore(startDate));
                    boolean beforeEnd = (endDate == null || !created.isAfter(endDate));
                    return afterStart && beforeEnd;
                })
                .toList();
    }

}
