package com.asd.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardMetricDto {
   private long totalUsers;
   private long totalAccounts;
   private long totalTransactions;

   private long totalActions;

}

