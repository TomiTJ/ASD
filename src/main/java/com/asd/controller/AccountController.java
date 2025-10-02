package com.asd.controller;

import com.asd.model.Account;
import com.asd.model.User;
import com.asd.repository.AccountRepository;
import com.asd.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final AccountRepository accounts;
    private final UserRepository users;

    public AccountController(AccountRepository accounts, UserRepository users) {
        this.accounts = accounts;
        this.users = users;
    }

    /** Guard: must be logged-in ADMIN. Return a redirect string if blocked; otherwise null. */
    private String requireAdmin(HttpSession session) {
        Object userId = session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        Object role = session.getAttribute("userRole");
        if (!(role instanceof String) || !"ADMIN".equals(role)) {
            return "redirect:/dashboard?error=forbidden";
        }
        return null;
    }

    /** DTO for list rows */
    public record AccountRow(
            Long id,
            int accountNumber,
            Integer userId,
            String customerName,
            String customerEmail,
            Account.AccountType accountType,
            Account.AccountStatus accountStatus,
            double balance
    ) {}

    /** GET /accounts — list + optional search */
    @GetMapping
    public String list(Model model, HttpSession session,
                       @RequestParam(value = "q", required = false) String q,
                       @RequestParam(value = "msg", required = false) String msg) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        model.addAttribute("currentUserId", session.getAttribute("userId"));
        if (msg != null) model.addAttribute("msg", msg);

        Map<Integer, User> userMap = users.findAll().stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        String needle = q == null ? null : q.trim().toLowerCase();

        List<AccountRow> rows = accounts.findAll().stream()
                .filter(a -> {
                    if (needle == null || needle.isBlank()) return true;
                    boolean byNumber = String.valueOf(a.getAccountNumber()).contains(needle);
                    User u = userMap.get(a.getUserId());
                    boolean byName = (u != null && u.getFullName() != null &&
                            u.getFullName().toLowerCase().contains(needle));
                    return byNumber || byName;
                })
                .map(a -> {
                    User u = userMap.get(a.getUserId());
                    return new AccountRow(
                            a.getId(),
                            a.getAccountNumber(),
                            a.getUserId(),
                            u != null ? u.getFullName() : "(unlinked)",
                            u != null ? u.getEmail() : "",
                            a.getAccountType(),
                            a.getAccountStatus(),
                            a.getBalance()
                    );
                }).toList();

        model.addAttribute("accounts", rows);
        return "account"; // <— list page view name
    }

    /** GET /accounts/create */
    @GetMapping("/create")
    public String createForm(Model model, HttpSession session) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        Account a = new Account();
        a.setAccountStatus(Account.AccountStatus.OPEN);
        a.setAccountType(Account.AccountType.SAVINGS);
        a.setBalance(0);

        model.addAttribute("account", a);
        model.addAttribute("customers", users.findAll());
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "account-create"; // <— create page view name
    }

    /** POST /accounts/create */
    @PostMapping("/create")
    public String create(@ModelAttribute("account") Account form,
                         HttpSession session,
                         RedirectAttributes ra) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        if (form.getBalance() < 0) form.setBalance(0);
        accounts.save(form);
        ra.addAttribute("msg", "Account created successfully!");
        return "redirect:/accounts";
    }

    /** GET /accounts/edit/{id} */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        Account a = accounts.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account Id: " + id));

        model.addAttribute("account", a);
        model.addAttribute("customers", users.findAll());
        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("userRole", session.getAttribute("userRole"));
        return "account-edit"; // <— edit page view name
    }

    /** POST /accounts/edit/{id} */
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("account") Account updated,
                         HttpSession session,
                         RedirectAttributes ra) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        Account a = accounts.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account Id: " + id));

        a.setAccountNumber(updated.getAccountNumber());
        a.setUserId(updated.getUserId());
        a.setAccountType(updated.getAccountType());
        a.setAccountStatus(updated.getAccountStatus());
        a.setBalance(Math.max(0, updated.getBalance()));

        accounts.save(a);
        ra.addAttribute("msg", "Account updated successfully!");
        return "redirect:/accounts";
    }

    /** POST /accounts/delete/{id} */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         HttpSession session,
                         RedirectAttributes ra) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        accounts.deleteById(id);
        ra.addAttribute("msg", "Account deleted successfully!");
        return "redirect:/accounts";
    }

    /** POST /accounts/freeze/{id} */
    @PostMapping("/freeze/{id}")
    public String freeze(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        Account a = accounts.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account Id: " + id));
        a.setAccountStatus(Account.AccountStatus.FROZEN);
        accounts.save(a);
        ra.addAttribute("msg", "Account frozen.");
        return "redirect:/accounts";
    }

    /** POST /accounts/close/{id} */
    @PostMapping("/close/{id}")
    public String close(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        Account a = accounts.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account Id: " + id));
        a.setAccountStatus(Account.AccountStatus.CLOSED);
        accounts.save(a);
        ra.addAttribute("msg", "Account closed.");
        return "redirect:/accounts";
    }
}
