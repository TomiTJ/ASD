package com.asd.services;

import com.asd.dto.AccountDetail;
import com.asd.dto.AccountDto;

import java.util.List;

public interface AccountService {
    void createAccount(AccountDto accountDto);
    List<AccountDto> findAllAccounts();
    List<AccountDto> searchAccounts(String search);
    void updateAccount(AccountDto accountDto);
    void freezeAccount(Long id);
    void unfreezeAccount(Long id);
    void closeAccount(Long id);
    void deleteAccount(Long id);

    // Joint account methods
    AccountDetail getAccountDetail(Long accountId);
    void linkCustomerToAccount(Long accountId, Long customerId);
    void unlinkCustomerFromAccount(Long accountId, Long customerId);
    List<Long> getJointCustomerIds(Long accountId);
}