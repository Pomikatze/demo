package com.example.demo.controller;

import com.example.demo.jwt.JwtRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Api для работы с другими операциями")
@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class MainController {

    @Operation(summary = "Открыть форму для аутентификации")
    @GetMapping("/auth")
    public String auth(Model model) {
        model.addAttribute("jwtRequest", new JwtRequest());
        return "auth";
    }
}
