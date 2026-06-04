package com.asd.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDto {
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
}
