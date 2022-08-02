package com.example.demo.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(DemoException.class)
    public String handle(Model model, Exception ex) {
        model.addAttribute("exception", ex);
        return "exception";
    }
}
