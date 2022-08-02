package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "Дто сущности 'Заказ'")
@Data
public class OrderDto {

    @Schema(description = "Идентификатор записи")
    private Long id;

    @Schema(description = "Идентификатор заказа")
    private Long orderId;

    @Schema(description = "Дата заказа")
    private String date;

    @Schema(description = "Продукт")
    private String product;

    @Schema(description = "Минимальный вес")
    private String minWeight;

    @Schema(description = "Количество товаров")
    private Long count;

    @Schema(description = "Название организации")
    private String organization;

    @Schema(description = "Идентификатор пользователя")
    private Long user;
}
