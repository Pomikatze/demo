package com.example.demo.service;

import com.example.demo.dto.OrderDto;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.OrganizationEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.DemoException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.OrganizationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrganizationRepository organizationRepository;

    private final OrderMapper orderMapper;

    private final UserService userService;

    public OrderDto findById(Long id, Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        OrderEntity orderEntity = orderRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new DemoException("Заказ не найден"));
        return orderMapper.toDto(orderEntity);
    }

    public List<OrderDto> findAll(Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        List<OrderEntity> orderEntities = orderRepository.findAllByUserId(user.getId())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        List<OrderDto> orderDtos = new ArrayList<>();

        for (OrderEntity o : orderEntities) {
            orderDtos.add(orderMapper.toDto(o));
        }

        return orderDtos;
    }

    public String save(OrderDto orderDto, Principal principal) {
        OrderEntity orderEntity = orderMapper.toEntity(orderDto);
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не найден"));
        orderEntity.setUser(user);
        orderRepository.save(orderEntity);
        return"complete";
    }

    public String delete(OrderDto orderDto) {
        if (orderRepository.existsById(orderDto.getId())) {
            OrderEntity orderEntity = orderMapper.toEntity(orderDto);
            orderRepository.delete(orderEntity);
            return"complete";
        } else {
            return"false";
        }
    }

    public List<OrderDto> findAllByOrderId(Long id, Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        List<OrderEntity> orderEntities = orderRepository.findAllByOrderIdAndUserId(id, user.getId());
        List<OrderDto> orderDtos = new ArrayList<>();

        for (OrderEntity o : orderEntities) {
            orderDtos.add(orderMapper.toDto(o));
        }

        return orderDtos;
    }

    public List<OrderDto> findAllByOrganizationName(String name, Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        OrganizationEntity organization = organizationRepository.findByNameAndUserId(name, user.getId());
        List<OrderEntity> orderEntities = orderRepository.findAllByOrganizationAndUserId(organization, user.getId());
        List<OrderDto> orderDtos = new ArrayList<>();

        for (OrderEntity o : orderEntities) {
            orderDtos.add(orderMapper.toDto(o));
        }

        return orderDtos;
    }
}
