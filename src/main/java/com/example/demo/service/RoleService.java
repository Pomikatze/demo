package com.example.demo.service;

import com.example.demo.entity.RoleEntity;
import com.example.demo.exception.DemoException;
import com.example.demo.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository repository;

    public RoleEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new DemoException("role не найден, id = " + id));
    }

    public RoleEntity findByName(String name) {
        return repository.findByName(name);
    }

    public Set<RoleEntity> findAllByName(String name) {
        return repository.findAllByName(name);
    }

    public RoleEntity save(RoleEntity role) {
        return repository.save(role);
    }

    public boolean existByName(String name) {
        return repository.existsByName(name);
    }
}
