package com.asd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping({"/loan", "/loan.html"})
    public String loan() {
        return "loan"; // resolves templates/loan.html
    }
}
