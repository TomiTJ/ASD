package com.asd;

import com.asd.controller.AccountController;
import com.asd.dto.AccountDto;
import com.asd.model.Account;
import com.asd.repository.CustomerRepository;
import com.asd.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountControllerTests {

    private AccountService accountService;
    private CustomerRepository customerRepository;
    private AccountController controller;

    @BeforeEach
    void setUp() {
        accountService = Mockito.mock(AccountService.class);
        customerRepository = Mockito.mock(CustomerRepository.class);
        controller = new AccountController(accountService, customerRepository);
    }

    @Test
    void testCreateAccount() {
        AccountDto accountDto = new AccountDto();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 1L);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        doNothing().when(accountService).createAccount(any(AccountDto.class));

        String result = controller.createAccount(accountDto, session, redirectAttributes);

        assertEquals("redirect:/account", result);
        verify(accountService, times(1)).createAccount(any(AccountDto.class));
    }

    @Test
    void testUpdateAccountProfile() {
        Long accountId = 1L;
        String accountStatus = "OPEN";
        BigDecimal balance = new BigDecimal("1000.00");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 1L);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        doNothing().when(accountService).updateAccount(any(AccountDto.class));

        String result = controller.updateAccount(accountId, accountStatus, balance, session, redirectAttributes);

        assertEquals("redirect:/account", result);
        verify(accountService, times(1)).updateAccount(any(AccountDto.class));
    }
}