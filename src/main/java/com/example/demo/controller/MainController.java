package com.example.demo.controller;

import com.example.demo.config.feign.service.TestFeignApi;
import com.example.demo.dto.OrderDto;
import com.example.demo.dto.OrganizationDto;
import com.example.demo.dto.RabbitMessage;
import com.example.demo.dto.StorageMessage;
import com.example.demo.jwt.JwtRequest;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Tag(name = "Api для работы с другими операциями")
@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class MainController {

    private final TestFeignApi feignApi;

    private final OrganizationService organizationService;

    private final OrderService orderService;

    @Operation(summary = "Открыть форму для аутентификации")
    @GetMapping("/auth")
    public String auth(Model model) {
        model.addAttribute("jwtRequest", new JwtRequest());
        return "auth";
    }

    @Operation
    @GetMapping("/send/{organization}/{orderId}")
    public String send(Principal principal, @PathVariable String organization, @PathVariable String orderId) {

        OrganizationDto organizationDto = organizationService.findByName(organization, principal);
        List<OrderDto> orderDto = orderService.findAllByOrderId(Long.valueOf(orderId), principal);

        StorageMessage storageMessage = new StorageMessage();
        storageMessage.setOrganization(organizationDto);
        storageMessage.setOrder(orderDto);

        feignApi.send(new RabbitMessage(storageMessage));
        return "redirect:http://localhost:8082/test/api/send/" + organization + "/" + orderId;
    }
}
