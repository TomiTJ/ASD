package com.asd.services;

import com.asd.dto.TransactionDto;
import com.asd.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> findallTransactions();
    List<TransactionDto> findFilteredTransactions(String search);

}

