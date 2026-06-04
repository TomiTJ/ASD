package com.asd.services.impl;

import com.asd.dto.TransactionDto;
import com.asd.dto.TransferRequestDto;
import com.asd.dto.TransferResultDto;
import com.asd.model.Account;
import com.asd.model.Customer;
import com.asd.model.Transaction;
import com.asd.repository.AccountRepository;
import com.asd.repository.CustomerRepository;
import com.asd.repository.TransactionRepository;
import com.asd.services.TransactionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountRepository accountRepository,
                                  CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
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

    @Override
    @Transactional
    public TransferResultDto transfer(TransferRequestDto request) {
        validateTransferRequest(request);

        Account fromAccount = accountRepository.findByIdForUpdate(request.getFromAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        Account toAccount = accountRepository.findByIdForUpdate(request.getToAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

        validateTransferAccounts(fromAccount, toAccount, request.getAmount());

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        Customer customer = customerRepository.findById(fromAccount.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Source account customer not found"));

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setType(Transaction.TransactionType.TRANSFER);
        transaction.setAmount(request.getAmount());
        transaction.setStatus(Transaction.TransactionStatus.COMPLETED);
        transaction.setCreatedAt(OffsetDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return TransferResultDto.builder()
                .transactionId(savedTransaction.getId())
                .fromAccountId(fromAccount.getId())
                .toAccountId(toAccount.getId())
                .amount(savedTransaction.getAmount())
                .fromAccountBalance(fromAccount.getBalance())
                .toAccountBalance(toAccount.getBalance())
                .status(savedTransaction.getStatus().name())
                .build();
    }

    private TransactionDto mapToTransactionData(Transaction transaction) {
        TransactionDto transactionDto = TransactionDto.builder()
                .id(transaction.getId())
                .customer(transaction.getCustomer())
                .fromAccountNumber(transaction.getFromAccount() != null ? transaction.getFromAccount().getAccountNumber() : null)
                .toAccountNumber(transaction.getToAccount() != null ? transaction.getToAccount().getAccountNumber() : null)
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .build();
        return transactionDto;
    }

    private void validateTransferRequest(TransferRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Transfer request is required");
        }
        if (request.getFromAccountId() == null) {
            throw new IllegalArgumentException("Source account is required");
        }
        if (request.getToAccountId() == null) {
            throw new IllegalArgumentException("Destination account is required");
        }
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new IllegalArgumentException("Source and destination accounts must be different");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
    }

    private void validateTransferAccounts(Account fromAccount, Account toAccount, BigDecimal amount) {
        if (fromAccount.getAccountStatus() != Account.AccountStatus.OPEN) {
            throw new IllegalArgumentException("Source account must be open");
        }
        if (toAccount.getAccountStatus() != Account.AccountStatus.OPEN) {
            throw new IllegalArgumentException("Destination account must be open");
        }
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }
}
