package com.example.demo.service.search;

import com.example.demo.dto.search.OrganizationSearchParam;
import com.example.demo.entity.OrganizationEntity;
import com.example.demo.searchSpec.DtoQueryParser;
import com.example.demo.searchSpec.core.JpaSpecificationsBuilder;
import com.example.demo.searchSpec.core.api.QueryParser;
import com.example.demo.service.api.search.OrganizationSearchWithSpecService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Сервис для генерации спецификации.
 */
@Service
public class OrganizationSearchWithSpecServiceImpl implements OrganizationSearchWithSpecService {

    @Override
    public Specification<OrganizationEntity> generateSpec(OrganizationSearchParam searchParam) {
        QueryParser<OrganizationSearchParam> parser = new DtoQueryParser<>();
        JpaSpecificationsBuilder<OrganizationEntity> specBuilder = new JpaSpecificationsBuilder<>(OrganizationEntity.class);
        return specBuilder.build(parser.parse(searchParam));
    }
}
