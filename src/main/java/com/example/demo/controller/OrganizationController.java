package com.example.demo.controller;

import com.example.demo.dto.OrganizationDto;
import com.example.demo.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Api для работы с сущностью 'Организация'")
@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @Operation(summary = "Сохранить организацию")
    @PostMapping("/organization/save")
    public String saveOrganization(OrganizationDto dto, Principal principal) {
        organizationService.save(dto, principal);
        return "redirect:http://localhost:8082/test/api/organization";
    }

    @Operation(summary = "Открыть форму для просмотра организаций пользователя")
    @GetMapping("/organization")
    public String organization(Model model, Principal principal) {
        List<OrganizationDto> dto = organizationService.findAll(principal);
        OrganizationDto organizationDto = new OrganizationDto();
        model.addAttribute("organization", dto);
        model.addAttribute("organizationDto", organizationDto);
        model.addAttribute("principal", principal.getName());
        return "organization";
    }

    @Operation(summary = "Открыть форму для просмотра конкретной организации")
    @GetMapping("/organization/{organization}")
    public String organizationId(@PathVariable String organization, Model model, Principal principal) {
        OrganizationDto dto = organizationService.findByName(organization);
        List<OrganizationDto> list = new ArrayList<>();
        OrganizationDto organizationDto = new OrganizationDto();
        list.add(dto);
        model.addAttribute("organization", list);
        model.addAttribute("organizationDto", organizationDto);
        model.addAttribute("principal", principal.getName());
        return "organization";
    }

    @Operation(summary = "Удалить организацию")
    @GetMapping("/organization/delete/{id}")
    public String deleteOrganization(@PathVariable String id) {
        Long organizationId = Long.valueOf(id);
        organizationService.deleteById(organizationId);
        return "redirect:http://localhost:8082/test/api/organization";
    }

    @Operation(summary = "Открыть форму для обновления организации")
    @GetMapping("/organization/update/{id}")
    public String update(@PathVariable String id, Model model, Principal principal) {
        Long organizationId = Long.valueOf(id);
        OrganizationDto organization = organizationService.findById(organizationId, principal);

        model.addAttribute("organization", organization);
        model.addAttribute("organizationDto", organization);
        model.addAttribute("principal", principal.getName());
        return "changeOrganization";
    }

    @Operation(summary = "Обновить организацию")
    @PostMapping("/organization/update")
    public String update(OrganizationDto organizationDto, Principal principal) {
        organizationService.save(organizationDto, principal);
        return "redirect:http://localhost:8082/test/api/organization";
    }
}
