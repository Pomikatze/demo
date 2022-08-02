package com.example.demo.mapper;

import com.example.demo.dto.PlanDto;
import com.example.demo.entity.PlanEntity;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class PlanMapper {

    private final ModelMapper modelMapper;

    private final UserService userService;

    public PlanDto toDto(PlanEntity entity) {
        entity.setUser(null);
        PlanDto dto = modelMapper.map(entity, PlanDto.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        if(entity.getToDoDttm() != null) {
            dto.setToDoDttm(entity.getToDoDttm().format(formatter));
        }
        if (entity.getUser() != null) {
            dto.setUser(entity.getUser().getId());
        }
        return dto;
    }

    public PlanEntity toEntity(PlanDto dto) {
        dto.setUser(null);
        PlanEntity entity = modelMapper.map(dto, PlanEntity.class);
        if (!dto.getToDoDttm().isEmpty()){
            entity.setToDoDttm(LocalDateTime.parse(dto.getToDoDttm()));
        }
        if (dto.getUser() != null) {
            entity.setUser(userService.findById(dto.getUser()));
        }
        return entity;
    }
}
