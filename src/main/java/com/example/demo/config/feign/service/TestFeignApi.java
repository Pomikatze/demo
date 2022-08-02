package com.example.demo.config.feign.service;

import com.example.demo.dto.Page;
import feign.Headers;
import feign.RequestLine;

public interface TestFeignApi {

    @RequestLine("GET /admin/log")
    @Headers("Content-Type: application/json")
    Page getLogs();
}
