package com.asd.dto;
import com.asd.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
@Builder
public class TransactionDto {
    private int id;
    private BigDecimal amount;
    private Transaction.TransactionType type;
    private Customer customer;
    private Transaction.TransactionStatus status;
    private OffsetDateTime createdAt;

    public String getDateCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy ");
        return createdAt.format(formatter);
    }
    public String getTimeCreatedAt(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return createdAt.format(formatter);
    }

    }
