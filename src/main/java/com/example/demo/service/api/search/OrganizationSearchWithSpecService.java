package com.example.demo.service.api.search;

import com.example.demo.dto.search.OrganizationSearchParam;
import com.example.demo.entity.OrganizationEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * Сервис для генерации спецификации.
 */
public interface OrganizationSearchWithSpecService {

    /**
     * Генерания спецификации для организаций.
     *
     * @param searchParam параметры поиска
     * @return спецификация
     */
    Specification<OrganizationEntity> generateSpec(OrganizationSearchParam searchParam);
}
