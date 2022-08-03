package com.example.demo.config.feign.service;

import com.example.demo.dto.Page;
import com.example.demo.dto.RabbitMessage;
import feign.Headers;
import feign.RequestLine;

public interface TestFeignApi {

    @RequestLine("GET /admin/log")
    @Headers("Content-Type: application/json")
    Page getLogs();

    @RequestLine("POST /rabbit/send")
    @Headers("Content-Type: application/json")
    void send(RabbitMessage message);
}
