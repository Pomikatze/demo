package com.example.demo.mapper;

import com.example.demo.dto.OrganizationDto;
import com.example.demo.entity.OrganizationEntity;
import com.example.demo.repositories.ChainRepository;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrganizationMapper {

    private final ModelMapper modelMapper;

    private final ChainRepository chainRepository;

    private final UserService userService;

    public OrganizationDto toDto(OrganizationEntity entity) {
        entity.setUser(null);
        OrganizationDto dto = modelMapper.map(entity, OrganizationDto.class);
        if (entity.getChain() != null) {
            dto.setChain(entity.getChain().getNameChain());
        }
        if (entity.getUser() != null) {
            dto.setUser(entity.getUser().getId());
        }
        return dto;
    }

    public OrganizationEntity toEntity(OrganizationDto dto) {
        dto.setUser(null);
        OrganizationEntity entity = modelMapper.map(dto, OrganizationEntity.class);
        if (!dto.getChain().isEmpty()) {
            entity.setChain(chainRepository.findByNameChain(dto.getChain()));
        }
        if (dto.getUser() != null) {
            entity.setUser(userService.findById(dto.getUser()));
        }
        return entity;
    }
}
