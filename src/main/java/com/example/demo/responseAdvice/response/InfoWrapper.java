package com.example.demo.responseAdvice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс обертка для стандартных ответов.
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoWrapper {

    @Schema(description = "Код сообщения", nullable = true)
    private String code;
    @Schema(description = "Описание сообщения", nullable = true)
    private String msg;

}
