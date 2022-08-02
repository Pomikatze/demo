package com.example.demo.repositories;

import com.example.demo.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<PlanEntity, Long> {

    List<PlanEntity> findAllByUserIdAndToDoDttmBetween(Long userId, LocalDateTime after, LocalDateTime before);

    Optional<List<PlanEntity>> findAllByUserId(Long userId);

    Optional<PlanEntity> findByIdAndUserId(Long id, Long userId);
}
