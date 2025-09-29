package com.asd.controller;

import com.asd.model.Account;
import com.asd.repository.AccountRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {

        this.accountRepository = accountRepository;
    }

    /*@GetMapping("/account")
    public String Account(Model model) {
        List<Account> account = accountRepository.findAll();
        model.addAttribute("accounts", accounts);
        return "account";
    } */
    //found error
    @RequestMapping("/account")
    public String start(Model model) {

        return "account";
    }
}
