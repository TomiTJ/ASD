package com.asd.controller;

import com.asd.model.Action;
import com.asd.model.ResourceType;

import com.asd.services.AuditService;
import com.asd.model.User;
import com.asd.repository.UserRepository;
import com.asd.services.AuditService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    public AuthController(UserRepository users, PasswordEncoder passwordEncoder, AuditService auditService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
    }

    // POST /login: verify against DB and create a session
    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session) {

        var user = users.findByEmailIgnoreCase(email).orElse(null);
        if (user == null) return "redirect:/login?error=notfound";

        if (user.getStatus() == User.Status.DEACTIVATED) {
            return "redirect:/login?error=inactive";
        }

        // 🔑 Use passwordEncoder to check hashed password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "redirect:/login?error=badcreds";
        }

        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getFullName());
        session.setAttribute("userRole", user.getRole().name());


        //DO NOT TOUCH AUDITSERVICES WITHOUT CONSULTING TOMI
        auditService.recordAction(user, Action.LOGIN, ResourceType.USER, UUID.randomUUID());
        return "redirect:/dashboard";
    }

    // POST /logout: clear session
    @PostMapping("/logout")
    public String doLogout(HttpSession session) {


        User user = (User) session.getAttribute("userId");
        auditService.recordAction(user, Action.LOGOUT, ResourceType.USER, UUID.randomUUID());
        session.invalidate();
        return "redirect:/login?logout";
    }
}

