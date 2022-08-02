package com.example.demo.controller;

import com.example.demo.dto.Page;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.WorkingBaseDto;
import com.example.demo.entity.LogEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.LogService;
import com.example.demo.service.SudisService;
import com.example.demo.service.UserService;
import com.example.demo.service.WorkingBaseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.bouncycastle.jcajce.provider.keystore.bc.BcKeyStoreSpi;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;

    private final LogService logService;

    private final WorkingBaseService workingBaseService;

    @Operation(summary = "Открыть форму Админа")
    @GetMapping("/admin")
    public String admin(Model model) {
        List<UserDto> userDtos = new ArrayList<>();
        List<UserEntity> userEntities = userService.findAll();
        List<LogEntity> logEntitys = logService.findAll();
        List<WorkingBaseDto> dto = workingBaseService.findAll();
        WorkingBaseDto baseDto = new WorkingBaseDto();

        for (UserEntity user : userEntities) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setName(user.getName());
            userDto.setPassword(user.getPassword());
            if (user.getRole().stream().findFirst().isPresent()) {
                userDto.setRole(user.getRole().stream().findFirst().get().getName());
            }
            userDtos.add(userDto);
        }

        model.addAttribute("workingBase", dto);
        model.addAttribute("baseDto", baseDto);
        model.addAttribute("userDtos", userDtos);
        model.addAttribute("logEntity", logEntitys);
        return "admin";
    }

    @Operation(summary = "Сохранить запись в базу")
    @PostMapping("/base/save")
    public String saveBase(WorkingBaseDto dto, Principal principal) {
        workingBaseService.save(dto, principal);
        return "redirect:http://localhost:8082/test/admin/admin";
    }
}
