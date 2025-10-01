package com.asd.dto;
import com.asd.model.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.time.Instant;
import java.util.stream.Collector;

import lombok.Data;

@Data
@Builder
public class TransactionDto {
    private int id;
    private Double amount;
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
