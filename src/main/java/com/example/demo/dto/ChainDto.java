package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Дто сущности 'Сеть'")
@Data
public class ChainDto {

    @Schema(description = "Идентификатор записи")
    private Long id;

    @Schema(description = "Имя сети")
    private String nameChain;

    @Schema(description = "Имя менеджера")
    private String nameManager;

    @Schema(description = "Номер телефона")
    private String phone;

    @Schema(description = "Почта")
    private String mail;

    @Schema(description = "Идентификатор пользователя")
    private Long user;
}
