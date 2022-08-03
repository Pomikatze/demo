package com.example.demo.rabbitMQ.controller;

import com.example.demo.dto.RabbitMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/rabbit")
public class MessageController {

    private String hostname = "Unknown";

    private final AmqpTemplate template;

    @Autowired
    public MessageController(AmqpTemplate template) {
        this.template = template;
    }

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestBody RabbitMessage message) {
        template.convertAndSend("storage", message);

        try {
            InetAddress address;
            address = InetAddress.getLocalHost();
            hostname = address.getHostName();

        } catch (UnknownHostException ex) {
            System.out.println("Hostname can not be resolved");
        }

        return ResponseEntity.ok(hostname + ": Success sent to queue");
    }
}
