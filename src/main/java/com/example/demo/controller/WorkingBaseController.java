package com.example.demo.controller;

import com.example.demo.dto.WorkingBaseDto;
import com.example.demo.exception.DemoException;
import com.example.demo.service.WorkingBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "Api для работы с сущностью 'База'")
@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class WorkingBaseController {

    private final WorkingBaseService workingBaseService;

    @Operation(summary = "Сохранить запись в базу")
    @PostMapping("/base/save")
    public String saveBase(WorkingBaseDto dto, Principal principal) {
        workingBaseService.saveWithDate(dto, principal);
        return "redirect:http://localhost:8082/test/api/base";
    }

    @Operation(summary = "Открыть форму для просмотра базы")
    @GetMapping("/base")
    public String base(Model model, Principal principal) {
        List<WorkingBaseDto> dto = workingBaseService.findAll(principal);
        WorkingBaseDto baseDto = new WorkingBaseDto();
        model.addAttribute("workingBase", dto);
        model.addAttribute("baseDto", baseDto);
        model.addAttribute("principal", principal.getName());
        return "base";
    }

    @Operation(summary = "Открыть форму для просмотря всей базы")
    @GetMapping("/base/all")
    public String baseAll(Model model, Principal principal) {
        List<WorkingBaseDto> dto = workingBaseService.findAll();
        model.addAttribute("workingBase", dto);
        model.addAttribute("principal", principal.getName());
        return "baseAll";
    }

    @Operation(summary = "Удалить запись из базы")
    @GetMapping("/base/delete/{id}")
    public String deleteBase(@PathVariable String id) {
        Long baseId = Long.valueOf(id);
        workingBaseService.deleteById(baseId);
        return "redirect:http://localhost:8082/test/api/base";
    }

    @Operation(summary = "Открыть форму для обновления записи из базы")
    @GetMapping("/base/update/{id}")
    public String update(@PathVariable String id, Model model, Principal principal) {
        Long baseId = Long.valueOf(id);
        WorkingBaseDto baseDto = workingBaseService.findById(baseId, principal);
        model.addAttribute("baseDto", baseDto);
        model.addAttribute("base", baseDto);
        model.addAttribute("principal", principal.getName());
        return "changeBase";
    }

    @Operation(summary = "Добавить в свою базу сущность")
    @GetMapping("base/add/{id}")
    public String addBase(@PathVariable String id, Principal principal) {
        workingBaseService.addUser(id, principal);
        return "redirect:http://localhost:8082/test/api/base/all";
    }

    @Operation(summary = "Обновить базу")
    @PostMapping("/base/update")
    public String update(WorkingBaseDto baseDto, Principal principal) {
        workingBaseService.save(baseDto, principal);
        return "redirect:http://localhost:8082/test/api/base";
    }
}
