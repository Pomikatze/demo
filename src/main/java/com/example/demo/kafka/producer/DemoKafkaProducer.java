package com.example.demo.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DemoKafkaProducer {

    @Value("${spring.kafka.topic.demo}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendDemoMessage(String message) {
        String key = UUID.randomUUID().toString();
        kafkaTemplate.send(topic, key, message);
    }
}
