package com.example.demo.controller;

import com.example.demo.dto.OrganizationDto;
import com.example.demo.dto.search.OrganizationSearchParam;
import com.example.demo.paging.DemoPageRequest;
import com.example.demo.paging.DemoPageResponse;
import com.example.demo.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для поиска по параметрам.
 */
@RestController
@RequestMapping("/search")
@AllArgsConstructor
public class SearchController {

    private final OrganizationService organizationService;

    /**
     * Поиск организаций по параметрам.
     *
     * @param searchParam параметры поиска
     * @param pageRequest параметры пагинации
     * @return список организаций
     */
    @Operation(summary = "Поиск организаций по параметрам")
    @PostMapping("/organization")
    public DemoPageResponse<OrganizationDto> searchOrganizationWithSpec(@RequestBody OrganizationSearchParam searchParam,
                                                                        DemoPageRequest pageRequest) {
        return organizationService.searchWithSpec(searchParam, pageRequest.toPageRequest());
    }
}
