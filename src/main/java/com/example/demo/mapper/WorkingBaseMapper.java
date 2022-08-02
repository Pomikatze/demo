package com.example.demo.mapper;

import com.example.demo.dto.OrganizationDto;
import com.example.demo.dto.WorkingBaseDto;
import com.example.demo.entity.OrganizationEntity;
import com.example.demo.entity.WorkingBaseEntity;
import com.example.demo.exception.DemoException;
import com.example.demo.repositories.ChainRepository;
import com.example.demo.repositories.OrganizationRepository;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Component
@AllArgsConstructor
public class WorkingBaseMapper {

    private final ModelMapper modelMapper;

    private final OrganizationRepository organizationRepository;

    private final ChainRepository chainRepository;

    private final UserService userService;


    public WorkingBaseDto toDto(WorkingBaseEntity entity) {
        WorkingBaseDto dto = modelMapper.map(entity, WorkingBaseDto.class);
        if (entity.getChain() != null){
            dto.setChain(entity.getChain().getNameChain());
        }
        if (entity.getOrganization() != null) {
            dto.setOrganization(entity.getOrganization().getName());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        if (dto.getCallDttm() != null) {
            dto.setCallDttm(entity.getCallDttm().format(formatter));
        }
        if (dto.getShipDttm() != null) {
            dto.setShipDttm(entity.getShipDttm().format(formatter));
        }
        if (dto.getNeedCallDttm() != null) {
            dto.setNeedCallDttm(entity.getNeedCallDttm().format(formatter));
        }
        if (entity.getUser() != null) {
            dto.setUser(entity.getUser().getName());
        }
        return dto;
    }

    public WorkingBaseEntity toEntity(WorkingBaseDto dto) {
        WorkingBaseEntity entity = modelMapper.map(dto, WorkingBaseEntity.class);
        if (!dto.getChain().isEmpty()) {
            entity.setChain(chainRepository.findByNameChain(dto.getChain()));
        }
        if (!dto.getOrganization().isEmpty()) {
            entity.setOrganization(organizationRepository.findByName(dto.getOrganization()));
        }
        if (dto.getUser() != null) {
            entity.setUser(userService.findByUsername(dto.getUser())
                    .orElseThrow(() -> new DemoException("Пользователь не найден")));
        }
        return entity;
    }
}
