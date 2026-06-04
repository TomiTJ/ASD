package com.asd.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransferResultDto {
    private int transactionId;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private BigDecimal fromAccountBalance;
    private BigDecimal toAccountBalance;
    private String status;
}
