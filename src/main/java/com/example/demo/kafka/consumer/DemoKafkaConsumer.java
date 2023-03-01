package com.example.demo.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public class DemoKafkaConsumer {

    @Value("${spring.kafka.topic.demo}")
    private String topic;

    @KafkaListener(topics = "demo")
    public void listen(String msg) {
        log.info("В топик " + topic + " поступило сообщение: " + msg);
    }
}
