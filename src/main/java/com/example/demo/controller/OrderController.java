package com.example.demo.controller;

import com.example.demo.dto.OrderDto;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Tag(name = "Api для работы с сущностью 'Заказ'")
@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final OrganizationService organizationService;

    @Operation(summary = "Открыть форму со всеми заказами пользователя")
    @GetMapping("/order")
    public String order(Model model, Principal principal) {
        List<OrderDto> dto = orderService.findAll(principal);
        model.addAttribute("order", dto);
        model.addAttribute("principal", principal.getName());
        return "order";
    }

    @Operation(summary = "Открыть форму со всеми заказами пользователя по конкретной организации")
    @GetMapping("/order/{organization}")
    public String oder(@PathVariable String organization, Model model, Principal principal) throws IOException {
        List<OrderDto> orderDtos = orderService.findAllByOrganizationName(organization, principal);
        createXls(orderDtos);
        model.addAttribute("id", organization);
        model.addAttribute("order", orderDtos);
        model.addAttribute("organization", organization);
        model.addAttribute("principal", principal.getName());
        return "order";
    }

    @Operation(summary = "Открыть форму с конкретным заказом")
    @GetMapping("/order/get/{id}")
    public String oderById(@PathVariable String id, Model model, Principal principal) throws IOException {
        List<OrderDto> orderDtos = orderService.findAllByOrderId(Long.valueOf(id), principal);
        createXls(orderDtos);
        model.addAttribute("order", orderDtos);
        model.addAttribute("id", id);
        model.addAttribute("organization", orderDtos.get(0).getOrganization());
        model.addAttribute("principal", principal.getName());
        return "order";
    }

    @Operation(summary = "Открыть форму для сохранения енового заказа")
    @GetMapping("/order/create/{organization}/{orderId}")
    public String orderCreate(@PathVariable String organization, Model model,
                              @PathVariable String orderId, Principal principal) {

        OrderDto orderDto = new OrderDto();
        Date date = new Date();
        List<OrderDto> order = new ArrayList<>();

        orderDto.setOrganization(organization);

        if (!orderId.equals("0")) {
            order = orderService.findAllByOrderId(Long.valueOf(orderId), principal);
            orderDto.setOrderId(Long.valueOf(orderId));
            model.addAttribute("orderDto", orderDto);
            model.addAttribute("order", order);
            model.addAttribute("organization", organization);
            return "orderCreate";
        }

        Long id = date.getTime();
        orderDto.setOrderId(id);

        model.addAttribute("orderDto", orderDto);
        model.addAttribute("order", order);
        model.addAttribute("organization", organization);
        model.addAttribute("principal", principal.getName());
        return "orderCreate";
    }

    @Operation(summary = "Сохранить заказ")
    @PostMapping("/order/save")
    public String orderSave(OrderDto orderDto, Principal principal) {
        orderService.save(orderDto, principal);
        return "redirect:http://localhost:8082/test/api/order/create/" +
                URLEncoder.encode(orderDto.getOrganization(), StandardCharsets.UTF_8) + "/" +
                orderDto.getOrderId().toString();
    }

    @Operation(summary = "Открыть форму для просмотра файла с заказом")
    @GetMapping("/file/{id}")
    public String file(@PathVariable String id, Principal principal, Model model) {
        if (organizationService.existsByName(id)) {
            List<OrderDto> orderDtos = orderService.findAllByOrganizationName(id, principal);
            model.addAttribute("order", orderDtos);
            return "file";
        }
        List<OrderDto> orderDtos = orderService.findAllByOrderId(Long.valueOf(id), principal);
        model.addAttribute("order", orderDtos);
        return "file";
    }

    public void createXls(List<OrderDto> list) throws IOException {
        HSSFWorkbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Заказы");
        HSSFCellStyle cellStyleHead = createCellStyleHead(book);
        HSSFCellStyle cellStyle = createCellStyle(book);

        Row headRow = sheet.createRow(0);

        Cell headOrderId = headRow.createCell(0);
        headOrderId.setCellValue("Идентификатор");
        headOrderId.setCellStyle(cellStyleHead);

        Cell headOrganization = headRow.createCell(1);
        headOrganization.setCellValue("Организация");
        headOrganization.setCellStyle(cellStyleHead);

        Cell headDate = headRow.createCell(2);
        headDate.setCellValue("Дата");
        headDate.setCellStyle(cellStyleHead);

        Cell headMinWeight = headRow.createCell(3);
        headMinWeight.setCellValue("Минимальный вес");
        headMinWeight.setCellStyle(cellStyleHead);

        Cell headCount = headRow.createCell(4);
        headCount.setCellValue("Количество");
        headCount.setCellStyle(cellStyleHead);

        for (int i = 0; i < list.size(); i++) {
            Row row = sheet.createRow(i + 1);

            Cell orderId = row.createCell(0);
            orderId.setCellValue(list.get(i).getOrderId().toString());
            orderId.setCellStyle(cellStyle);

            Cell organization = row.createCell(1);
            organization.setCellValue(list.get(i).getOrganization());
            organization.setCellStyle(cellStyle);

            Cell date = row.createCell(2);
            DataFormat format = book.createDataFormat();
            CellStyle dataStyle = book.createCellStyle();
            dataStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
            date.setCellStyle(dataStyle);
            date.setCellValue(list.get(i).getDate());
            date.setCellStyle(cellStyle);

            Cell minWeight = row.createCell(3);
            minWeight.setCellValue(list.get(i).getMinWeight());
            minWeight.setCellStyle(cellStyle);

            Cell count = row.createCell(4);
            count.setCellValue(list.get(i).getCount());
            count.setCellStyle(cellStyle);
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);

        File file = new File("src/main/resources/test.xls");
        book.write(new FileOutputStream(file));
        book.close();
    }

    private HSSFCellStyle createCellStyleHead(HSSFWorkbook book) {
        short black = IndexedColors.BLACK.getIndex();

        HSSFCellStyle style = book.createCellStyle();

        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);

        style.setTopBorderColor(black);
        style.setRightBorderColor(black);
        style.setBottomBorderColor(black);
        style.setLeftBorderColor(black);

        return style;
    }

    private HSSFCellStyle createCellStyle(HSSFWorkbook book) {
        short black = IndexedColors.BLACK.getIndex();

        HSSFCellStyle style = book.createCellStyle();

        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);

        style.setTopBorderColor(black);
        style.setRightBorderColor(black);
        style.setBottomBorderColor(black);
        style.setLeftBorderColor(black);

        return style;
    }
}