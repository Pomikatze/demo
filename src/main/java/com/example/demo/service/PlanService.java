package com.example.demo.service;

import com.example.demo.dto.PlanDto;
import com.example.demo.entity.PlanEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.DemoException;
import com.example.demo.mapper.PlanMapper;
import com.example.demo.repositories.PlanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    private final PlanMapper planMapper;

    private final UserService userService;

    public List<PlanDto> findAll(Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        List<PlanEntity> planEntities = planRepository.findAllByUserId(user.getId())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        List<PlanDto> planDtos = new ArrayList<>();

        for (PlanEntity p : planEntities) {
            planDtos.add(planMapper.toDto(p));
        }

        return planDtos;
    }

    public PlanDto findById(Long id, Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        return planMapper.toDto(planRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new DemoException("Ежедневник не найден")));
    }

    public String delete(PlanDto dto) {
        PlanEntity entity = planMapper.toEntity(dto);
        planRepository.delete(entity);
        return "complete";
    }

    public String save(PlanDto dto, Principal principal) {
        PlanEntity entity = planMapper.toEntity(dto);
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не найден"));
        entity.setUser(user);
        entity.setCheck("N");
        planRepository.save(entity);
        return "Complete";
    }

    public List<PlanDto> findByDateTime(LocalDateTime date, Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        LocalDateTime after = date.withHour(0);
        LocalDateTime before = date.plusDays(1).withHour(0);
        List<PlanEntity> planEntities = planRepository.findAllByUserIdAndToDoDttmBetween(user.getId(), after, before);
        List<PlanDto> planDtos = new ArrayList<>();

        for (PlanEntity p : planEntities) {
            planDtos.add(planMapper.toDto(p));
        }

        return planDtos;
    }

    public List<PlanDto> findByDate(LocalDate date, Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        List<PlanEntity> planEntities = planRepository.findAllByUserIdAndToDoDttmBetween(user.getId(),
                date.atStartOfDay(), date.atTime(23, 59));
        List<PlanDto> planDtos = new ArrayList<>();

        for (PlanEntity p : planEntities) {
            planDtos.add(planMapper.toDto(p));
        }

        return planDtos;
    }

    public String saveWithDate(PlanDto dto, Principal principal) {
        PlanEntity entity = planMapper.toEntity(dto);
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не найден"));
        entity.setUser(user);
        if(!dto.getToDoDttm().isEmpty()) {
            entity.setToDoDttm(LocalDateTime.parse(dto.getToDoDttm()));
        }
        entity.setCheck("N");
        planRepository.save(entity);
        return "Complete";
    }

    public String deleteById(Long id) {
        if(planRepository.existsById(id)) {
            planRepository.deleteById(id);
            return "true";
        }
        return "false";
    }

    public PlanDto updateCheck(Long id, String check) {
        if (planRepository.existsById(id)) {
            PlanEntity plan = planRepository.getOne(id);
            plan.setCheck(check);
            planRepository.save(plan);
            return planMapper.toDto(plan);
        }
        return null;
    }
}
