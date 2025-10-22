package com.asd.services;

import com.asd.dto.AccountDto;
import java.util.List;

public interface AccountService {
    void createAccount(AccountDto accountDto);
    List<AccountDto> findAllAccounts();
    List<AccountDto> searchAccounts(String search);
    void updateAccount(AccountDto accountDto);
    void freezeAccount(Long id);
    void unfreezeAccount(Long id);  // ADDED: Unfreeze method
    void closeAccount(Long id);
    void deleteAccount(Long id);
}