package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Дто сущности 'База'")
@Data
public class WorkingBaseDto {

    @Schema(description = "Идентификатор записи")
    private Long id;

    @Schema(description = "Название организации")
    private String organization;

    @Schema(description = "Название сети")
    private String chain;

    @Schema(description = "Дата последнего звонка")
    private String callDttm;

    @Schema(description = "Дата последней отгрузки")
    private String shipDttm;

    @Schema(description = "Дата следуюущего звонка")
    private String needCallDttm;

    @Schema(description = "Комментарий")
    private String comment;

    @Schema(description = "Пользователь")
    private String user;
}
