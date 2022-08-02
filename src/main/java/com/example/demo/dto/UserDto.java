package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Дто сущности 'Пользователь'")
@Data
public class UserDto {

    @Schema(description = "Идентификатор записи")
    private Long id;

    @Schema(description = "Логин")
    private String name;

    @Schema(description = "Пароль")
    private String password;

    @Schema(description = "Роль")
    private String role;
}
