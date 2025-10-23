package com.asd.services.impl;

import com.asd.dto.AccountDetail;
import com.asd.dto.AccountDto;
import com.asd.model.Account;
import com.asd.model.Customer;
import com.asd.model.JointAccount;
import com.asd.repository.AccountRepository;
import com.asd.repository.CustomerRepository;
import com.asd.repository.JointAccountRepository;
import com.asd.services.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final JointAccountRepository jointAccountRepository;

    public AccountServiceImpl(AccountRepository accountRepository,
                              CustomerRepository customerRepository,
                              JointAccountRepository jointAccountRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.jointAccountRepository = jointAccountRepository;
    }

    @Override
    public void createAccount(AccountDto accountDto) {
        Customer customer = customerRepository.findById(accountDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

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
        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(account);
    }

    @Override
    public void freezeAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setAccountStatus(Account.AccountStatus.FROZEN);
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    @Override
    public void unfreezeAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setAccountStatus(Account.AccountStatus.OPEN);
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    @Override
    public void closeAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setAccountStatus(Account.AccountStatus.CLOSED);
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        jointAccountRepository.deleteByAccountId(id);
        accountRepository.delete(account);
    }

    @Override
    public AccountDetail getAccountDetail(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        AccountDetail detail = new AccountDetail();
        detail.setId(account.getId());
        detail.setAccountNumber(account.getAccountNumber());
        detail.setAccountType(account.getAccountType().name());
        detail.setAccountStatus(account.getAccountStatus().name());
        detail.setBalance(account.getBalance());

        customerRepository.findById(account.getCustomerId()).ifPresent(customer -> {
            detail.setPrimaryCustomerName(customer.getName());
        });

        List<JointAccount> jointAccounts = jointAccountRepository.findByAccountId(accountId);
        detail.setJoint(!jointAccounts.isEmpty());

        List<AccountDetail.JointCustomerDto> jointCustomers = jointAccounts.stream()
                .map(ja -> {
                    Customer customer = customerRepository.findById(ja.getCustomerId())
                            .orElse(null);
                    if (customer != null) {
                        return new AccountDetail.JointCustomerDto(
                                customer.getId().toString(),
                                customer.getName(),
                                ja.getLinkedDate().toString(),
                                ja.getLinkedTime().toString()
                        );
                    }
                    return null;
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());

        detail.setJointCustomers(jointCustomers);

        // Get available customers (not already linked)
        List<Long> linkedCustomerIds = jointAccounts.stream()
                .map(JointAccount::getCustomerId)
                .collect(Collectors.toList());
        linkedCustomerIds.add(account.getCustomerId()); // Exclude primary customer too

        List<AccountDetail.CustomerSimpleDto> availableCustomers = customerRepository.findAll().stream()
                .filter(c -> !linkedCustomerIds.contains(c.getId()))
                .map(c -> new AccountDetail.CustomerSimpleDto(
                        c.getId().toString(),
                        c.getName(),
                        c.getEmail()
                ))
                .collect(Collectors.toList());

        detail.setAvailableCustomers(availableCustomers);

        return detail;
    }

    @Override
    @Transactional
    public void linkCustomerToAccount(Long accountId, Long customerId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (jointAccountRepository.existsByAccountIdAndCustomerId(accountId, customerId)) {
            throw new RuntimeException("Customer is already linked to this account");
        }

        JointAccount jointAccount = new JointAccount();
        jointAccount.setAccountId(accountId);
        jointAccount.setCustomerId(customerId);
        jointAccount.setIsJoint(true);
        jointAccount.setLinkedDate(LocalDate.now());
        jointAccount.setLinkedTime(LocalTime.now());

        jointAccountRepository.save(jointAccount);
    }

    @Override
    @Transactional
    public void unlinkCustomerFromAccount(Long accountId, Long customerId) {
        if (!jointAccountRepository.existsByAccountIdAndCustomerId(accountId, customerId)) {
            throw new RuntimeException("Customer is not linked to this account");
        }

        jointAccountRepository.deleteByAccountIdAndCustomerId(accountId, customerId);
    }

    @Override
    public List<Long> getJointCustomerIds(Long accountId) {
        return jointAccountRepository.findByAccountId(accountId).stream()
                .map(JointAccount::getCustomerId)
                .collect(Collectors.toList());
    }

    private AccountDto convertToDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setCustomerId(account.getCustomerId());
        dto.setAccountType(account.getAccountType());
        dto.setAccountStatus(account.getAccountStatus());
        dto.setBalance(account.getBalance());

        customerRepository.findById(account.getCustomerId()).ifPresent(customer -> {
            dto.setCustomerName(customer.getName());
            dto.setCustomerEmail(customer.getEmail());
        });

        return dto;
    }
}