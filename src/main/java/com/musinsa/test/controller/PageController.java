package com.musinsa.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String welcome(Model model) {
        model.addAttribute("message", "Hello, Musinsa!");
        return "welcome";
    }

    @GetMapping("/implement1")
    public String implement1(Model model) {
        model.addAttribute("message", "Hello, Musinsa!");
        return "implement1";
    }

    @GetMapping("/implement2")
    public String implement2(Model model) {
        model.addAttribute("message", "Hello, Musinsa!");
        return "implement2";
    }

    @GetMapping("/implement3")
    public String implement3(Model model) {
        model.addAttribute("message", "Hello, Musinsa!");
        return "implement3";
    }

    @GetMapping("/implement4")
    public String implement4(Model model) {
        model.addAttribute("message", "Hello, Musinsa!");
        return "implement4";
    }
}
