package com.asd.controller;

import com.asd.dto.AccountDto;
import com.asd.model.Account;
import com.asd.model.Customer;
import com.asd.services.AccountService;
import com.asd.repository.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final CustomerRepository customerRepository;

    public AccountController(AccountService accountService, CustomerRepository customerRepository) {
        this.accountService = accountService;
        this.customerRepository = customerRepository;
    }

    /** Guard: must be logged-in. Return a redirect string if blocked; otherwise null. */
    private String requireLogin(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        return null;
    }

    @GetMapping
    public String listAccounts(Model model, HttpSession session,
                               @RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "msg", required = false) String msg) {
        String guard = requireLogin(session);
        if (guard != null) return guard;

        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));

        if (msg != null) model.addAttribute("msg", msg);

        List<AccountDto> accounts;
        if (search != null && !search.trim().isEmpty()) {
            accounts = accountService.searchAccounts(search);
            model.addAttribute("search", search);
        } else {
            accounts = accountService.findAllAccounts();
        }

        model.addAttribute("accounts", accounts);
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("newAccount", new AccountDto());

        return "account";
    }

    @PostMapping("/create")
    public String createAccount(@ModelAttribute AccountDto accountDto,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        String guard = requireLogin(session);
        if (guard != null) return guard;

        try {
            // Generate account number if not provided
            if (accountDto.getAccountNumber() == null || accountDto.getAccountNumber().isEmpty()) {
                accountDto.setAccountNumber(generateAccountNumber());
            }

            accountService.createAccount(accountDto);
            redirectAttributes.addAttribute("msg", "Account created successfully!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("msg", "Error creating account: " + e.getMessage());
        }

        return "redirect:/account";
    }

    @PostMapping("/update/{id}")
    public String updateAccount(@PathVariable Long id,
                                @RequestParam String accountStatus,
                                @RequestParam BigDecimal balance,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        String guard = requireLogin(session);
        if (guard != null) return guard;

        try {
            AccountDto dto = new AccountDto();
            dto.setId(id);
            dto.setAccountStatus(Account.AccountStatus.valueOf(accountStatus));
            dto.setBalance(balance);

            accountService.updateAccount(dto);
            redirectAttributes.addAttribute("msg", "Account updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("msg", "Error updating account: " + e.getMessage());
        }

        return "redirect:/account";
    }

    @PostMapping("/freeze/{id}")
    public String freezeAccount(@PathVariable Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        String guard = requireLogin(session);
        if (guard != null) return guard;

        try {
            accountService.freezeAccount(id);
            redirectAttributes.addAttribute("msg", "Account frozen successfully!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("msg", "Error freezing account: " + e.getMessage());
        }

        return "redirect:/account";
    }

    @PostMapping("/close/{id}")
    public String closeAccount(@PathVariable Long id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        String guard = requireLogin(session);
        if (guard != null) return guard;

        try {
            accountService.closeAccount(id);
            redirectAttributes.addAttribute("msg", "Account closed successfully!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("msg", "Error closing account: " + e.getMessage());
        }

        return "redirect:/account";
    }

    @PostMapping("/delete/{id}")
    public String deleteAccount(@PathVariable Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        String guard = requireLogin(session);
        if (guard != null) return guard;

        String role = (String) session.getAttribute("userRole");
        if (!"ADMIN".equals(role)) {
            redirectAttributes.addAttribute("msg", "Only admins can delete accounts!");
            return "redirect:/account";
        }

        try {
            accountService.deleteAccount(id);
            redirectAttributes.addAttribute("msg", "Account deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("msg", "Error deleting account: " + e.getMessage());
        }

        return "redirect:/account";
    }

    private String generateAccountNumber() {
        // Simple account number generation (you can make this more sophisticated)
        return "ACC" + System.currentTimeMillis();
    }

}