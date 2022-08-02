package com.example.demo.repositories;

import com.example.demo.entity.SudisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SudisRepository extends JpaRepository<SudisEntity, Long> {

    List<SudisEntity> findBySessionId(String sessionId);
}
