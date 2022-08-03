package com.example.demo.rabbitMQ.listener;

import com.example.demo.dto.RabbitMessage;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class StorageListener {

    @RabbitListener(queues = "storage")
    public void processStorageQueue(RabbitMessage message) {
        // Какая-то логика Склада
        System.out.println("!!!!Сообщение на склад принято!!!!" + message.toString());
    }
}
