package com.example.demo.controller;

import com.example.demo.dto.OrderDto;
import com.example.demo.dto.OrganizationDto;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Tag(name = "Api для работы с документами")
@Controller
@RequestMapping("/doc")
@AllArgsConstructor
public class InvoiceController {

    private final OrderService orderService;

    private final OrganizationService organizationService;

    @Operation(summary = "Просмотр документа")
    @GetMapping("/{id}")
    public String create(@PathVariable String id, Model model, Principal principal) {
        if (organizationService.existsByName(id)) {
            model.addAttribute("order", "Ошибка! Выберите конкретный заказ.");
            return "invoice";
        }

        List<OrderDto> orderDtos = orderService.findAllByOrderId(Long.valueOf(id), principal);
        String date = LocalDate.now().toString();
        model.addAttribute("date", date);

        if (!orderDtos.isEmpty()) {
            OrganizationDto organizationDto = organizationService.findByName(orderDtos.get(0).getOrganization(), principal);
            String organization = organizationDto.getName();
            String address = organizationDto.getAddress();
            String number = orderDtos.get(0).getOrderId().toString();
            model.addAttribute("organization", organization);
            model.addAttribute("address", address);
            model.addAttribute("number", number);
        }

        for (int i = 1; i <= orderDtos.size(); i++) {
            orderDtos.get(i - 1).setOrderId(Integer.toUnsignedLong(i));
        }

        model.addAttribute("order", orderDtos);
        return "invoice";
    }
}
