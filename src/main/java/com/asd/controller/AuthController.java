package com.asd.controller;

import com.asd.model.User;
import com.asd.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserRepository users;

    public AuthController(UserRepository users) {
        this.users = users;
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

        if (!user.getPassword().equals(password)) {
            return "redirect:/login?error=badcreds";
        }


        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getFullName());
        session.setAttribute("userRole", user.getRole().name());
        return "redirect:/dashboard";
    }

    // POST /logout: clear session
    @PostMapping("/logout")
    public String doLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}
