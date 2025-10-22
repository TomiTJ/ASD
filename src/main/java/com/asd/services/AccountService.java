package com.asd.services;

import com.asd.dto.AccountDto;
import java.util.List;

public interface AccountService {
    List<AccountDto> findAllAccounts();
    List<AccountDto> searchAccounts(String search);
    AccountDto findAccountById(Long id);
    AccountDto createAccount(AccountDto accountDto);
    AccountDto updateAccount(AccountDto accountDto);
    void freezeAccount(Long id);
    void closeAccount(Long id);
    void deleteAccount(Long id);
}