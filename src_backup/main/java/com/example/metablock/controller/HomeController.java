package com.example.metablock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Metablock 웹 애플리케이션");
        model.addAttribute("message", "Thymeleaf와 Spring Boot가 성공적으로 구성되었습니다!");
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "소개");
        model.addAttribute("message", "Metablock 프로젝트 소개 페이지입니다.");
        return "about";
    }
} 