package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Дто сущности 'Организация'")
@Data
public class OrganizationDto {

    @Schema(description = "Идентификатор записи")
    private Long id;

    @Schema(description = "Название организации")
    private String name;

    @Schema(description = "Название сети")
    private String chain;

    @Schema(description = "Адрес организации")
    private String address;

    @Schema(description = "Имя менеджера")
    private String nameManager;

    @Schema(description = "Номер телефона")
    private String phone;

    @Schema(description = "Почта")
    private String mail;

    @Schema(description = "Идентификатор пользователя")
    private Long user;
}
