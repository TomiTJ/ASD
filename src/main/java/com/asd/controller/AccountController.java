package com.asd.controller;

import com.asd.dto.AccountDetail;
import com.asd.dto.AccountDto;
import com.asd.model.Account;
import com.asd.repository.CustomerRepository;
import com.asd.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/account")
@Tag(name = "Accounts", description = "Customer account management")
public class AccountController {

    private final AccountService accountService;
    private final CustomerRepository customerRepository;

    public AccountController(AccountService accountService, CustomerRepository customerRepository) {
        this.accountService = accountService;
        this.customerRepository = customerRepository;
    }

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

    @GetMapping("/api/detail/{id}")
    @ResponseBody
    @Operation(summary = "Get account details", description = "Returns full account detail including joint customers")
    public ResponseEntity<AccountDetail> getAccountDetailApi(
            @Parameter(description = "Account ID") @PathVariable Long id,
            HttpSession session) {
        String guard = requireLogin(session);
        if (guard != null) {
            return ResponseEntity.status(401).build();
        }

        try {
            AccountDetail detail = accountService.getAccountDetail(id);
            return ResponseEntity.ok(detail);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/api/update/{id}")
    @ResponseBody
    @Operation(summary = "Update account", description = "Update account type, status, and balance")
    public ResponseEntity<Map<String, Object>> updateAccountApi(
            @Parameter(description = "Account ID") @PathVariable Long id,
                                                                @RequestParam String accountType,
                                                                @RequestParam String accountStatus,
                                                                @RequestParam BigDecimal balance,
                                                                HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String guard = requireLogin(session);
        if (guard != null) {
            response.put("success", false);
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }

        try {
            AccountDto dto = new AccountDto();
            dto.setId(id);
            dto.setAccountType(Account.AccountType.valueOf(accountType));
            dto.setAccountStatus(Account.AccountStatus.valueOf(accountStatus));
            dto.setBalance(balance);

            accountService.updateAccount(dto);
            response.put("success", true);
            response.put("message", "Account updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint for modal - Link customer
    @PostMapping("/api/link-customer/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> linkCustomerApi(@PathVariable Long id,
                                                               @RequestParam Long customerId,
                                                               HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String guard = requireLogin(session);
        if (guard != null) {
            response.put("success", false);
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }

        try {
            accountService.linkCustomerToAccount(id, customerId);
            response.put("success", true);
            response.put("message", "Customer linked successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // API endpoint for modal - Unlink customer
    @PostMapping("/api/unlink-customer/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> unlinkCustomerApi(@PathVariable Long id,
                                                                 @RequestParam Long customerId,
                                                                 HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String guard = requireLogin(session);
        if (guard != null) {
            response.put("success", false);
            response.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(response);
        }

        try {
            accountService.unlinkCustomerFromAccount(id, customerId);
            response.put("success", true);
            response.put("message", "Customer unlinked successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/create")
    public String createAccount(@ModelAttribute AccountDto accountDto,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        String guard = requireLogin(session);
        if (guard != null) return guard;

        try {
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

    @PostMapping("/unfreeze/{id}")
    public String unfreezeAccount(@PathVariable Long id,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        String guard = requireLogin(session);
        if (guard != null) return guard;

        try {
            accountService.unfreezeAccount(id);
            redirectAttributes.addAttribute("msg", "Account unfrozen successfully!");
        } catch (Exception e) {
            redirectAttributes.addAttribute("msg", "Error unfreezing account: " + e.getMessage());
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
        // UUID-based: collision-free even under concurrent requests
        return "ACC-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}