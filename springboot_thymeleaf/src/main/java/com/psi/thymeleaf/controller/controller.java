package com.psi.thymeleaf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/leaf")
public class controller {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("hello", "hello thymeleaf");
        List<String> list = new ArrayList<>();
        list.add("刻晴");
        list.add("胡桃");
        list.add("七七");
        list.add("雷神");
        model.addAttribute("list", list);
        model.addAttribute("time", new Date());
        model.addAttribute("age", 18);
        model.addAttribute("str1", "王者");
        model.addAttribute("str2", "荣耀");
        return "demo";
    }
}
