package com.asd.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        return "dashboard";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

//    @GetMapping("/users")
//    public String users() {
//        return "users";
//    }
}


