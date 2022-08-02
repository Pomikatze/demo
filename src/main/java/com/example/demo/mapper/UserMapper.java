package com.example.demo.mapper;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.DemoException;
import com.example.demo.service.RoleService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    private final RoleService roleService;

    public UserDto toDto(UserEntity entity) {
        UserDto dto = new UserDto();
        dto.setRole(entity.getRole().stream().findFirst()
                .orElseThrow(() -> new DemoException("Ошибка парсинга роли")).getName());
        entity.setRole(null);
        return modelMapper.map(entity, UserDto.class);
    }

    public UserEntity toEntity(UserDto dto) {
        UserEntity entity = new UserEntity();

        if (roleService.existByName(dto.getRole())) {
            Set<RoleEntity> roles = roleService.findAllByName(dto.getRole());
            entity.setRole(roles);
        }

        dto.setRole(null);
        return modelMapper.map(dto, UserEntity.class);
    }
}
