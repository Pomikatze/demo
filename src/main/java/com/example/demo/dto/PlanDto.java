package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Дто сущности 'Ежедневник'")
@Data
public class PlanDto {

    @Schema(description = "Идентификатор записи")
    private Long id;

    @Schema(description = "Что сделать")
    private String toDo;

    @Schema(description = "Когда сделать")
    private String toDoDttm;

    @Schema(description = "Отметка 'Выполнено'")
    private String check;

    @Schema(description = "Идентификатор пользователя")
    private Long user;
}
