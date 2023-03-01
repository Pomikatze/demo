package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.kafka.producer.DemoKafkaProducer;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final DemoKafkaProducer producer;

    @GetMapping("")
    public List<UserDto> getAll() {
        return userService.findAllDto();
    }

    @GetMapping("/message/{msg}")
    public void sendMessage(@PathVariable String msg) {
        producer.sendDemoMessage(msg);
    }
}
