package com.example.demo.repositories;

import com.example.demo.entity.WorkingBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkingBaseRepository extends JpaRepository<WorkingBaseEntity, Long> {

    Optional<WorkingBaseEntity> findByIdAndUserId(Long id, Long userId);

    Optional<List<WorkingBaseEntity>> findAllByUserId(Long userId);
}
