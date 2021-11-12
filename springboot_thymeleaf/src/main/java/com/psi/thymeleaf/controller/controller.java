package com.psi.thymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/leaf")
public class controller {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("hello", "hello thymeleaf");
        return "demo";
    }
}
