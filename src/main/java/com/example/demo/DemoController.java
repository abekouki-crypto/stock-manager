package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "ようこそ！Spring boot + Thymeleafへ！");
        return "index";
    }

    @RequestMapping("/input")
    public String input(Model model) {
        model.addAttribute("demoForm", new DemoForm());
        return "input";
    }

    @RequestMapping("/output")
    public String output(@ModelAttribute DemoForm demoForm, Model model) {
        model.addAttribute("demoForm", demoForm);
        return "output";
    }

}