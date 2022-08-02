package com.example.demo.repositories;

import com.example.demo.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);

    Set<RoleEntity> findAllByName(String name);

    boolean existsByName(String name);
}
