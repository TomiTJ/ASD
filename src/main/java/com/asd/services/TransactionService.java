package com.asd.services;

import com.asd.dto.TransactionDto;
import com.asd.dto.TransferRequestDto;
import com.asd.dto.TransferResultDto;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> findallTransactions();
    List<TransactionDto> findFilteredTransactions(String search, String type, String status);
    TransferResultDto transfer(TransferRequestDto request);

}
