package com.example.demo.repositories;

import com.example.demo.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long>,
        JpaSpecificationExecutor<OrganizationEntity> {

    OrganizationEntity findByNameAndUserId(String name, Long userId);

    Optional<OrganizationEntity> findByIdAndUserId(Long id, Long userId);

    Optional<List<OrganizationEntity>> findAllByUserId(Long userId);

    OrganizationEntity findByName(String name);

    boolean existsByName(String name);
}
