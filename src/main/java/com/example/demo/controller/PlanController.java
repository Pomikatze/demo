package com.example.demo.controller;

import com.example.demo.dto.PlanDto;
import com.example.demo.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Tag(name = "Api для работы с сущностью 'Ежедневник'")
@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class PlanController {

    private final PlanService planService;

    @Operation(summary = "Сохранить ежедневник")
    @PostMapping("/plan/save")
    public String savePlan(PlanDto dto, Principal principal) {
        planService.saveWithDate(dto, principal);
        return "redirect:http://localhost:8082/test/api/plan";
    }

    @Operation(summary = "Открыть форму для просмотра ежедневника")
    @GetMapping("/plan")
    public String plan(Model model, Principal principal) {
        List<PlanDto> dto = planService.findAll(principal);
        PlanDto planDto = new PlanDto();
        model.addAttribute("plan", dto);
        model.addAttribute("planDto", planDto);
        model.addAttribute("principal", principal.getName());
        return "plan";
    }

    @Operation(summary = "Открыть форму для просмотра по конкретной дате")
    @GetMapping("/plan/{plan}")
    public String planId(@PathVariable String plan, Model model, Principal principal) {

        List<PlanDto> dtos = planService.findByDateTime(LocalDateTime.parse(plan, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), principal);
        PlanDto planDto = new PlanDto();
        model.addAttribute("plan", dtos);
        model.addAttribute("planDto", planDto);
        model.addAttribute("principal", principal.getName());
        return "plan";
    }

    @Operation(summary = "Удалить запись")
    @GetMapping("/plan/delete/{id}")
    public String deletePlan(@PathVariable String id) {
        Long planId = Long.valueOf(id);
        planService.deleteById(planId);
        return "redirect:http://localhost:8082/test/api/plan";
    }

    @Operation(summary = "Открыть форму для обновления ежедневника")
    @GetMapping("/plan/update/{id}")
    public String update(@PathVariable String id, Model model, Principal principal) {
        Long planId = Long.valueOf(id);
        PlanDto plan = planService.findById(planId, principal);
        model.addAttribute("plan", plan);
        model.addAttribute("planDto", plan);
        model.addAttribute("principal", principal.getName());
        return "changePlan";
    }

    @Operation(summary = "Обновить ежедневник")
    @PostMapping("/plan/update")
    public String update(PlanDto planDto, Principal principal) {
        if (!planDto.getToDo().isEmpty() || !planDto.getToDoDttm().isEmpty()) {
            planService.save(planDto, principal);
            return "redirect:http://localhost:8082/test/api/plan";
        }
        return "redirect:http://localhost:8082/test/api/plan";
    }
}
