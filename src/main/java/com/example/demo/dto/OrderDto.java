package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Schema(description = "Дто сущности 'Заказ'")
@Data
public class OrderDto {

    @Schema(description = "Идентификатор записи")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "Идентификатор заказа")
    @JsonProperty("orderId")
    private Long orderId;

    @Schema(description = "Дата заказа")
    @JsonProperty("date")
    private String date;

    @Schema(description = "Продукт")
    @JsonProperty("product")
    private String product;

    @Schema(description = "Минимальный вес")
    @JsonProperty("minWeight")
    private String minWeight;

    @Schema(description = "Количество товаров")
    @JsonProperty("count")
    private Long count;

    @Schema(description = "Название организации")
    @JsonProperty("organization")
    private String organization;

    @Schema(description = "Идентификатор пользователя")
    @JsonProperty("user")
    private Long user;
}
