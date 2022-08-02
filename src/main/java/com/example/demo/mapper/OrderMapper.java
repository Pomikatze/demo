package com.example.demo.mapper;

import com.example.demo.dto.OrderDto;
import com.example.demo.dto.OrganizationDto;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.OrganizationEntity;
import com.example.demo.repositories.OrganizationRepository;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class OrderMapper {

    private final ModelMapper modelMapper;

    private final OrganizationRepository organizationRepository;

    private final UserService userService;

    public OrderEntity toEntity(OrderDto orderDto) {
        orderDto.setUser(null);
        OrderEntity entity = modelMapper.map(orderDto, OrderEntity.class);
        if(!orderDto.getOrganization().isEmpty()) {
            entity.setOrganization(organizationRepository.findByName(orderDto.getOrganization()));
        }
        if (!orderDto.getDate().isEmpty()) {
            entity.setDate(LocalDate.parse(orderDto.getDate()));
        }
        if (orderDto.getUser() != null) {
            entity.setUser(userService.findById(orderDto.getUser()));
        }
        return entity;
    }

    public OrderDto toDto(OrderEntity orderEntity) {
        orderEntity.setUser(null);
        OrderDto dto = modelMapper.map(orderEntity, OrderDto.class);
        if (orderEntity.getOrganization() != null) {
            dto.setOrganization(orderEntity.getOrganization().getName());
        }
        if (orderEntity.getUser() != null) {
            dto.setUser(orderEntity.getUser().getId());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if(orderEntity.getDate() != null) {
            dto.setDate(orderEntity.getDate().format(formatter));
        }
        return dto;
    }
}
