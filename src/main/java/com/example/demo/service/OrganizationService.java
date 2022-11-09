package com.example.demo.service;

import com.example.demo.dto.OrganizationDto;
import com.example.demo.dto.search.OrganizationSearchParam;
import com.example.demo.entity.OrganizationEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.DemoException;
import com.example.demo.mapper.OrganizationMapper;
import com.example.demo.paging.DemoPageResponse;
import com.example.demo.repositories.OrganizationRepository;
import com.example.demo.service.api.search.OrganizationSearchWithSpecService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper organizationMapper;

    private final UserService userService;

    private final OrganizationSearchWithSpecService searchWithSpecService;

    public Long getTotalOrganization() {
        return organizationRepository.count();
    }

    public OrganizationDto findById(Long id, Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        return organizationMapper
                .toDto(organizationRepository.findByIdAndUserId(id, user.getId())
                        .orElseThrow(() -> new DemoException("Организация не найдена")));
    }

    public OrganizationDto findByName (String name, Principal principal){
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        return organizationMapper.toDto(organizationRepository.findByNameAndUserId(name, user.getId()));
    }

    public OrganizationDto findByName (String name){
        return organizationMapper.toDto(organizationRepository.findByName(name));
    }

    public List<OrganizationDto> findAll(Principal principal) {
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        List<OrganizationEntity> organizationEntities = organizationRepository.findAllByUserId(user.getId())
                .orElseThrow(() -> new DemoException("Пользователь не зарегистрирован"));
        List<OrganizationDto> organizationDtos = new ArrayList<>();

        for (OrganizationEntity o : organizationEntities) {
            organizationDtos.add(organizationMapper.toDto(o));
        }

        return organizationDtos;
    }

    public String save(OrganizationDto organizationDto, Principal principal) {
        OrganizationEntity organization = organizationMapper.toEntity(organizationDto);
        UserEntity user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new DemoException("Пользователь не найден"));
        organization.setUser(user);
        organizationRepository.save(organization);
        return"complete";
    }

    public String delete(OrganizationDto organizationDto) {
        if (organizationRepository.existsById(organizationDto.getId())) {
            OrganizationEntity organization = organizationMapper.toEntity(organizationDto);
            organizationRepository.delete(organization);
            return "complete";
        } else {
            return"false";
        }
    }

    public String deleteById(Long id) {
        if (organizationRepository.existsById(id)) {
            organizationRepository.deleteById(id);
            return "true";
        }
        return "false";
    }

    public boolean existsByName(String name) {
        return organizationRepository.existsByName(name);
    }

    /**
     * Поиск организаций по спецификации.
     *
     * @param searchParam параметры поиска
     * @param pageable параметры пагинации
     * @return {@link DemoPageResponse} {@link OrganizationDto}
     */
    public DemoPageResponse<OrganizationDto> searchWithSpec(OrganizationSearchParam searchParam, Pageable pageable) {
        Specification<OrganizationEntity> spec = searchWithSpecService.generateSpec(searchParam);
        Page<OrganizationEntity> entities = organizationRepository.findAll(spec, pageable);
        List<OrganizationDto> content = new ArrayList<>(entities.map(organizationMapper::toDto).getContent());
        return new DemoPageResponse<>(entities.getTotalElements(), entities.getTotalPages(), content);
    }
}
