package com.asd.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardMetricDto {
   private long totalUsers;
   private long totalAccounts;
   private long totalTransactions;

}

