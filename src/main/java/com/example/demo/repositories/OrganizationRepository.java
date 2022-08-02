package com.example.demo.repositories;

import com.example.demo.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {

    OrganizationEntity findByNameAndUserId(String name, Long userId);

    Optional<OrganizationEntity> findByIdAndUserId(Long id, Long userId);

    Optional<List<OrganizationEntity>> findAllByUserId(Long userId);

    OrganizationEntity findByName(String name);

    boolean existsByName(String name);
}
