package com.example.demo.service;

import com.example.demo.entity.LogEntity;
import com.example.demo.repositories.LogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public void saveLog(LogEntity logEntity) {
        logRepository.save(logEntity);
    }

    public List<LogEntity> findAll() {
        return logRepository.findAll();
    }
}
