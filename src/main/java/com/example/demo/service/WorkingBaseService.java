package com.example.demo.service;

import com.example.demo.dto.WorkingBaseDto;
import com.example.demo.entity.ChainEntity;
import com.example.demo.entity.OrganizationEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.WorkingBaseEntity;
import com.example.demo.exception.DemoException;
import com.example.demo.mapper.WorkingBaseMapper;
import com.example.demo.repositories.ChainRepository;
import com.example.demo.repositories.OrganizationRepository;
import com.example.demo.repositories.WorkingBaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class WorkingBaseService {

    private final WorkingBaseRepository repository;

    private final WorkingBaseMapper mapper;

    private final UserService userService;

    public WorkingBaseDto findById(Long id, Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        WorkingBaseEntity workingBaseEntity = repository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new DemoException("База не найдена"));
        return mapper.toDto(workingBaseEntity);
    }

    public List<WorkingBaseDto> findAll(Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        List<WorkingBaseEntity> workingBaseEntities = repository.findAllByUserId(user.getId())
                .orElseThrow(() -> new DemoException("База не найдена"));
        List<WorkingBaseDto> workingBaseDtos = new ArrayList<>();

        for (WorkingBaseEntity o : workingBaseEntities) {
            workingBaseDtos.add(mapper.toDto(o));
        }

        return workingBaseDtos;
    }

    public List<WorkingBaseDto> findAll() {
        List<WorkingBaseEntity> baseEntities = repository.findAll();
        List<WorkingBaseDto> workingBaseDtos = new ArrayList<>();

        for (WorkingBaseEntity o : baseEntities) {
            workingBaseDtos.add(mapper.toDto(o));
        }

        return workingBaseDtos;
    }

    public String save(WorkingBaseDto workingBaseDto, Principal principal) {
        WorkingBaseEntity workingBaseEntity = mapper.toEntity(workingBaseDto);
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не найден"));

        if (user.getRole().isEmpty() || user.getRole().stream().findFirst().get().getName().equals("ROLE_ADMIN")) {
            workingBaseEntity.setUser(null);
        } else {
            workingBaseEntity.setUser(user);
        }

        repository.save(workingBaseEntity);
        return "complete";
    }

    public String addUser(String id, Principal principal) {
        WorkingBaseEntity workingBase = repository.getOne(Long.valueOf(id));

        workingBase.getChain().setUser(userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не найден")));
        workingBase.getOrganization().setUser(userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не найден")));
        workingBase.setUser(userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не найден")));

        repository.save(workingBase);

        return "Complete";
    }

    public String saveWithDate(WorkingBaseDto workingBaseDto, Principal principal) {

        WorkingBaseEntity workingBaseEntity = mapper.toEntity(workingBaseDto);

        String callDttm = workingBaseDto.getCallDttm();
        String shipDttm = workingBaseDto.getShipDttm();
        String needCallDttm = workingBaseDto.getNeedCallDttm();

        if (!callDttm.isEmpty()) {
            workingBaseEntity.setCallDttm(LocalDateTime.parse(callDttm));
        }

        if (!shipDttm.isEmpty()) {
            workingBaseEntity.setShipDttm(LocalDateTime.parse(shipDttm));
        }

        if (!needCallDttm.isEmpty()) {
            workingBaseEntity.setNeedCallDttm(LocalDateTime.parse(needCallDttm));
        }

        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не найден"));
        workingBaseEntity.setUser(user);
        repository.save(workingBaseEntity);
        return "complete";
    }

    public String delete(WorkingBaseDto workingBaseDto) {
        if (repository.existsById(workingBaseDto.getId())) {
            WorkingBaseEntity workingBaseEntity = mapper.toEntity(workingBaseDto);
            repository.delete(workingBaseEntity);
            return "complete";
        } else {
            return "false";
        }
    }

    public String deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "true";
        }
        return "false";
    }
}
