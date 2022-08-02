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
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Api для работы с сущностью 'Календарь'")
@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class CalendarController {

    private final PlanService planService;

    @Operation(summary = "Открыть форму с календарём")
    @GetMapping("/calendar")
    public String calendar(Model model, Principal principal) {
        List<PlanDto> plan = new ArrayList<>();
        model.addAttribute("plan", plan);
        model.addAttribute("principal", principal.getName());
        return "calendar";
    }

    @Operation(summary = "Открыть форму с календарём по конкретному числу")
    @GetMapping("/calendar/{date}")
    public String calendar(@PathVariable String date, Model model, Principal principal) {
        if (date.isEmpty()){
            List<PlanDto> plan = new ArrayList<>();
            model.addAttribute("plan", plan);
            model.addAttribute("principal", principal.getName());
            return "calendar";
        } else {
            List<PlanDto> plan = planService.findByDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")), principal);
            model.addAttribute("plan", plan);
            model.addAttribute("principal", principal.getName());
            return "calendar";
        }
    }

    @Operation(summary = "Отметить дело, как выполненное")
    @GetMapping("/calendar/do/{planId}")
    public String ready(@PathVariable Long planId, Model model, Principal principal) {
        PlanDto planDto = planService.updateCheck(planId, "Y");
        List<PlanDto> plan = planService.findByDateTime(LocalDateTime.parse(planDto.getToDoDttm(),
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), principal);
        model.addAttribute("plan", plan);
        model.addAttribute("principal", principal.getName());
        return "calendar";
    }
}
