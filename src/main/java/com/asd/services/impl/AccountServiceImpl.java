package com.asd.services.impl;

import com.asd.dto.AccountDto;
import com.asd.model.Account;
import com.asd.model.Customer;
import com.asd.repository.AccountRepository;
import com.asd.repository.CustomerRepository;
import com.asd.services.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<AccountDto> findAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(this::mapToAccountDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountDto> searchAccounts(String search) {
        List<Account> accounts = accountRepository.findAll();
        String searchLower = search.toLowerCase();

        return accounts.stream()
                .filter(account -> {
                    Customer customer = customerRepository.findById(account.getCustomerId()).orElse(null);
                    if (customer != null) {
                        return customer.getName().toLowerCase().contains(searchLower) ||
                                account.getAccountNumber().toLowerCase().contains(searchLower);
                    }
                    return account.getAccountNumber().toLowerCase().contains(searchLower);
                })
                .map(this::mapToAccountDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto findAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return mapToAccountDto(account);
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setCustomerId(accountDto.getCustomerId());
        account.setAccountType(accountDto.getAccountType());
        account.setAccountStatus(Account.AccountStatus.OPEN);
        account.setBalance(accountDto.getBalance() != null ? accountDto.getBalance() : java.math.BigDecimal.ZERO);

        Account savedAccount = accountRepository.save(account);
        return mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto updateAccount(AccountDto accountDto) {
        Account account = accountRepository.findById(accountDto.getId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (accountDto.getAccountStatus() != null) {
            account.setAccountStatus(accountDto.getAccountStatus());
        }
        if (accountDto.getBalance() != null) {
            account.setBalance(accountDto.getBalance());
        }
        if (accountDto.getAccountType() != null) {
            account.setAccountType(accountDto.getAccountType());
        }

        Account savedAccount = accountRepository.save(account);
        return mapToAccountDto(savedAccount);
    }

    @Override
    public void freezeAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setAccountStatus(Account.AccountStatus.FROZEN);
        accountRepository.save(account);
    }

    @Override
    public void closeAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setAccountStatus(Account.AccountStatus.CLOSED);
        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Account not found");
        }
        accountRepository.deleteById(id);
    }

    private AccountDto mapToAccountDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setCustomerId(account.getCustomerId());
        dto.setAccountType(account.getAccountType());
        dto.setAccountStatus(account.getAccountStatus());
        dto.setBalance(account.getBalance());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());

        // Fetch customer details
        Customer customer = customerRepository.findById(account.getCustomerId()).orElse(null);
        if (customer != null) {
            dto.setCustomerName(customer.getName());
            dto.setCustomerEmail(customer.getEmail());
        } else {
            dto.setCustomerName("Unknown");
            dto.setCustomerEmail("N/A");
        }

        return dto;
    }

}