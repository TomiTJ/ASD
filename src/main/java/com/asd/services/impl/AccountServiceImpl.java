package com.asd.services.impl;

import com.asd.dto.AccountDto;
import com.asd.model.Account;
import com.asd.model.Customer;
import com.asd.repository.AccountRepository;
import com.asd.repository.CustomerRepository;
import com.asd.services.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public void createAccount(AccountDto accountDto) {
        Customer customer = customerRepository.findById(accountDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + accountDto.getCustomerId()));

        Account account = new Account();
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setCustomerId(customer.getId());
        account.setAccountType(accountDto.getAccountType());
        account.setAccountStatus(Account.AccountStatus.OPEN);
        account.setBalance(accountDto.getBalance());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(account);
    }

    @Override
    public List<AccountDto> findAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountDto> searchAccounts(String search) {
        List<Account> accounts = accountRepository.findByAccountNumberContainingIgnoreCase(search);

        if (accounts.isEmpty()) {
            List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(search);
            if (!customers.isEmpty()) {
                List<Long> customerIds = customers.stream()
                        .map(Customer::getId)
                        .collect(Collectors.toList());
                accounts = accountRepository.findByCustomerIdIn(customerIds);
            }
        }

        return accounts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateAccount(AccountDto accountDto) {
        Account account = accountRepository.findById(accountDto.getId())
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountDto.getId()));

        if (accountDto.getAccountStatus() != null) {
            account.setAccountStatus(accountDto.getAccountStatus());
        }
        if (accountDto.getBalance() != null) {
            account.setBalance(accountDto.getBalance());
        }
        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(account);
    }

    @Override
    public void freezeAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        account.setAccountStatus(Account.AccountStatus.FROZEN);
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    // ADDED: Unfreeze method
    @Override
    public void unfreezeAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        account.setAccountStatus(Account.AccountStatus.OPEN);
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    @Override
    public void closeAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        account.setAccountStatus(Account.AccountStatus.CLOSED);
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        accountRepository.delete(account);
    }

    private AccountDto convertToDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setCustomerId(account.getCustomerId());
        dto.setAccountType(account.getAccountType());
        dto.setAccountStatus(account.getAccountStatus());
        dto.setBalance(account.getBalance());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());

        // Get customer details
        customerRepository.findById(account.getCustomerId()).ifPresent(customer -> {
            dto.setCustomerName(customer.getName());
            dto.setCustomerEmail(customer.getEmail());
        });

        return dto;
    }
}