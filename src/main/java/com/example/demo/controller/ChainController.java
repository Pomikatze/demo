package com.example.demo.controller;

import com.example.demo.dto.ChainDto;
import com.example.demo.service.ChainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Api для работы с сущностью 'Сеть'")
@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class ChainController {

    private final ChainService chainService;

    @Operation(summary = "Сохранить сеть")
    @PostMapping("/chain/save")
    public String saveChain(ChainDto dto, Principal principal) {
        chainService.save(dto, principal);
        return "redirect:http://localhost:8082/test/api/chain";
    }

    @Operation(summary = "Открыть форму для отображения всех сеток")
    @GetMapping("/chain")
    public String chain(Model model, Principal principal) {
        List<ChainDto> dto = chainService.findAll(principal);
        ChainDto chainDto = new ChainDto();
        model.addAttribute("chain", dto);
        model.addAttribute("chainDto", chainDto);
        model.addAttribute("principal", principal.getName());
        return "chain";
    }

    @Operation(summary = "Открыть форму с конкретной сетью")
    @GetMapping("/chain/{chain}")
    public String chainId(@PathVariable String chain, Model model, Principal principal) {
        ChainDto dto = chainService.findByName(chain);
        List<ChainDto> list = new ArrayList<>();
        ChainDto chainDto = new ChainDto();
        list.add(dto);
        model.addAttribute("chain", list);
        model.addAttribute("chainDto", chainDto);
        model.addAttribute("principal", principal.getName());
        return "chain";
    }

    @Operation(summary = "Удалить сеть")
    @GetMapping("/chain/delete/{id}")
    public String deleteChain(@PathVariable String id) {
        Long chainId = Long.valueOf(id);
        chainService.deleteById(chainId);
        return "redirect:http://localhost:8082/test/api/chain";
    }

    @Operation(summary = "Открыть форму для обновления сети")
    @GetMapping("/chain/update/{id}")
    public String update(@PathVariable String id, Model model, Principal principal) {
        Long chainId = Long.valueOf(id);
        ChainDto chain = chainService.findById(chainId, principal);
        model.addAttribute("chain", chain);
        model.addAttribute("chainDto", chain);
        model.addAttribute("principal", principal.getName());
        return "changeChain";
    }

    @Operation(summary = "Обновить сеть")
    @PostMapping("/chain/update")
    public String update(ChainDto chainDto, Principal principal) {
        chainService.save(chainDto, principal);
        return "redirect:http://localhost:8082/test/api/chain";
    }
}
