package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.DemoException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    final private UserRepository userRepository;

    final private RoleService roleService;

    final private UserMapper userMapper;


    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByName(username);
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new DemoException("user не найден, id = " + id));
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRole()));

    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<RoleEntity> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public List<UserDto> findAllDto() {
        List<UserEntity> entities = userRepository.findAll();
        List<UserDto> dtos = new ArrayList<>();

        for (UserEntity user : entities) {
            dtos.add(userMapper.toDto(user));
        }

        return dtos;
    }

    public void save(UserDto user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(user.getName());
        userEntity.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        Set<RoleEntity> set = new HashSet<>();
        set.add(roleService.findByName(user.getRole()));
        userEntity.setRole(set);
        userRepository.save(userEntity);
    }

    public void saveEntity(UserEntity user) {
        userRepository.save(user);
    }

    public boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }
}
