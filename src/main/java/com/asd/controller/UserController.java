package com.asd.controller;

import com.asd.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository users;

    public UserController(UserRepository users) {
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

    /** GET /users — list all users (ADMIN only) */
    @GetMapping
    public String listUsers(Model model, HttpSession session) {
        String guard = requireAdmin(session);
        if (guard != null) return guard;

        model.addAttribute("users", users.findAll());
        return "users";
    }
}
