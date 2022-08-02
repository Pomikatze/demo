package com.example.demo.repositories;

import com.example.demo.entity.ChainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChainRepository extends JpaRepository<ChainEntity, Long> {

    ChainEntity findByNameChainAndUserId(String name, Long userId);

    ChainEntity findByNameChain(String name);

    Optional<ChainEntity> findByIdAndUserId(Long id, Long userId);

    Optional<List<ChainEntity>> findAllByUserId(Long userId);
}
