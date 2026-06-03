package com.asd.services.impl;

import com.asd.dto.TransactionDto;
import com.asd.model.Transaction;
import com.asd.repository.TransactionRepository;
import com.asd.services.TransactionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    private static final int MAX_PAGE_SIZE = 200;

    @Override
    public List<TransactionDto> findallTransactions() {
        List<Transaction> transactions = transactionRepository
                .findAll(PageRequest.of(0, MAX_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt")))
                .getContent();
        return transactions.stream().map(this::mapToTransactionData).collect(Collectors.toList());
    }

    @Override
    public List<TransactionDto> findFilteredTransactions(String search, String type, String status) {
        // Load a bounded set and filter in-memory; replace with a @Query for large datasets
        List<Transaction> transactions = transactionRepository
                .findAll(PageRequest.of(0, MAX_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt")))
                .getContent();

        String keyword = (search == null) ? "" : search.trim().toLowerCase();
        String typeFilter = (type == null) ? "" : type.trim().toUpperCase();
        String statusFilter = (status == null) ? "" : status.trim().toUpperCase();

        return transactions.stream()
                .filter(t -> {
                    String customerName = t.getCustomer() != null ? t.getCustomer().getName().toLowerCase() : "";
                    String transactionId = String.valueOf(t.getId());
                    String transactionType = t.getType() != null ? t.getType().name() : "";
                    String transactionStatus = t.getStatus() != null ? t.getStatus().name() : "";
                    boolean matchesSearch = keyword.isEmpty()
                            || customerName.contains(keyword)
                            || transactionId.contains(keyword);

                    boolean matchesType = typeFilter.isEmpty()
                            || transactionType.equals(typeFilter);

                    boolean matchesStatus = statusFilter.isEmpty()
                            || transactionStatus.equals(statusFilter);

                    return matchesSearch && matchesType && matchesStatus;
                })
                .map(this::mapToTransactionData)
                .collect(Collectors.toList());
    }

    private TransactionDto mapToTransactionData(Transaction transaction) {
        TransactionDto transactionDto = TransactionDto.builder()
                .id(transaction.getId())
                .customer(transaction.getCustomer())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .build();
        return transactionDto;
    }
}
