package com.example.demo.service;

import com.example.demo.entity.SudisEntity;
import com.example.demo.repositories.SudisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SudisService {

    SudisRepository sudisRepository;

    @Autowired
    public SudisService(SudisRepository sudisRepository) {
        this.sudisRepository = sudisRepository;
    }

    public void saveSudis(String username, String token, HttpServletRequest request) {
        SudisEntity sudisEntity = new SudisEntity();
        sudisEntity.setLogin(username);
        sudisEntity.setToken(token);
        sudisEntity.setCreateDttm(LocalDateTime.now());
        if (!request.getHeader("Cookie").isEmpty()){
            String sessionId = request.getHeader("Cookie").substring(11);
            sudisEntity.setSessionId(sessionId);
        }
        sudisRepository.save(sudisEntity);
    }

    public List<SudisEntity> findAll() {
        return sudisRepository.findAll();
    }

    public SudisEntity findBySessionId(String sessionId) {
        List<SudisEntity> list = sudisRepository.findBySessionId(sessionId);
        if (list.isEmpty()) {
            return new SudisEntity();
        }
        return list.get(list.size() - 1);
    }
}
