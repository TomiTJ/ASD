package com.asd;

import com.asd.dto.TransferRequestDto;
import com.asd.dto.TransferResultDto;
import com.asd.model.Account;
import com.asd.model.Customer;
import com.asd.model.Transaction;
import com.asd.repository.AccountRepository;
import com.asd.repository.CustomerRepository;
import com.asd.repository.TransactionRepository;
import com.asd.services.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        accountRepository = mock(AccountRepository.class);
        customerRepository = mock(CustomerRepository.class);
        transactionService = new TransactionServiceImpl(transactionRepository, accountRepository, customerRepository);
    }

    @Test
    void transferDebitsSourceCreditsDestinationAndRecordsTransaction() {
        Account fromAccount = account(1L, 10L, "ACC-1", "1000.00", Account.AccountStatus.OPEN);
        Account toAccount = account(2L, 20L, "ACC-2", "250.00", Account.AccountStatus.OPEN);
        Customer customer = new Customer(10L, "Source Customer", "source@example.com");
        TransferRequestDto request = transferRequest(1L, 2L, "125.50");

        when(accountRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(toAccount));
        when(customerRepository.findById(10L)).thenReturn(Optional.of(customer));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransferResultDto result = transactionService.transfer(request);

        assertEquals(new BigDecimal("874.50"), fromAccount.getBalance());
        assertEquals(new BigDecimal("375.50"), toAccount.getBalance());
        assertEquals("COMPLETED", result.getStatus());

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();
        assertEquals(Transaction.TransactionType.TRANSFER, savedTransaction.getType());
        assertEquals(new BigDecimal("125.50"), savedTransaction.getAmount());
        assertEquals(fromAccount, savedTransaction.getFromAccount());
        assertEquals(toAccount, savedTransaction.getToAccount());
    }

    @Test
    void transferRejectsInsufficientFunds() {
        Account fromAccount = account(1L, 10L, "ACC-1", "25.00", Account.AccountStatus.OPEN);
        Account toAccount = account(2L, 20L, "ACC-2", "250.00", Account.AccountStatus.OPEN);

        when(accountRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(toAccount));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> transactionService.transfer(transferRequest(1L, 2L, "125.50"))
        );

        assertEquals("Insufficient funds", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void transferRejectsClosedDestinationAccount() {
        Account fromAccount = account(1L, 10L, "ACC-1", "500.00", Account.AccountStatus.OPEN);
        Account toAccount = account(2L, 20L, "ACC-2", "250.00", Account.AccountStatus.CLOSED);

        when(accountRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(toAccount));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> transactionService.transfer(transferRequest(1L, 2L, "125.50"))
        );

        assertEquals("Destination account must be open", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    private TransferRequestDto transferRequest(Long fromAccountId, Long toAccountId, String amount) {
        TransferRequestDto request = new TransferRequestDto();
        request.setFromAccountId(fromAccountId);
        request.setToAccountId(toAccountId);
        request.setAmount(new BigDecimal(amount));
        return request;
    }

    private Account account(Long id,
                            Long customerId,
                            String accountNumber,
                            String balance,
                            Account.AccountStatus status) {
        Account account = new Account();
        account.setId(id);
        account.setCustomerId(customerId);
        account.setAccountNumber(accountNumber);
        account.setAccountType(Account.AccountType.TRANSACTIONAL);
        account.setAccountStatus(status);
        account.setBalance(new BigDecimal(balance));
        return account;
    }
}
