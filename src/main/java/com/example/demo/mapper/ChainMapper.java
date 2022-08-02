package com.example.demo.mapper;

import com.example.demo.dto.ChainDto;
import com.example.demo.entity.ChainEntity;
import com.example.demo.service.ChainService;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.swing.event.ChangeEvent;

@Component
@AllArgsConstructor
public class ChainMapper {

    private final ModelMapper modelMapper;

    private final UserService userService;

    public ChainDto toDto(ChainEntity entity) {
        entity.setUser(null);
        ChainDto dto = modelMapper.map(entity, ChainDto.class);
        if (entity.getUser() != null) {
            dto.setUser(entity.getUser().getId());
        }
        return dto;
    }

    public ChainEntity toEntity(ChainDto dto) {
        dto.setUser(null);
        ChainEntity entity = modelMapper.map(dto, ChainEntity.class);
        if (dto.getUser() != null) {
            entity.setUser(userService.findById(dto.getUser()));
        }
        return entity;
    }
}
