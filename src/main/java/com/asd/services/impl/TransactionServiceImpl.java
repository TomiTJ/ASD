package com.asd.services.impl;

import com.asd.dto.TransactionDto;
import com.asd.model.Transaction;
import com.asd.repository.TransactionRepository;
import com.asd.services.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<TransactionDto> findallTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().map(transaction -> mapToTransactionData(transaction)).collect(Collectors.toList());
    }

    @Override
    public List<TransactionDto> findFilteredTransactions(String search) {
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream()
                .filter(t -> (search == null || search.isEmpty()
                        || t.getCustomer().getFull_name().toLowerCase().contains(search.toLowerCase())
                        || String.valueOf(t.getId()).contains(search)))
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
