package com.asd.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String login() { return "forward:/login.html"; }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        return "forward:/dashboard.html";
    }

    @GetMapping("/")
    public String root() { return "redirect:/login"; }

    // optional marker page you had:
    @GetMapping("/users")
    public String users() { return "forward:/users.html"; }
}

