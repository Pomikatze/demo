package com.example.demo.responseAdvice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс обертка для ответа с ошибкой.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorWrapper {

    @Schema(description = "Штамп времени", nullable = true)
    private String timestamp;

    @Schema(description = "Код ошибки", nullable = true)
    private String code;

    @Schema(description = "Описание ошибки", nullable = true)
    private String msg;

    @Schema(description = "Детальная информация об ошибке", nullable = true)
    private String details;

    public ErrorWrapper(String code, String msg, String details) {
        this.code = code;
        this.msg = msg;
        this.details = details;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        timestamp = sdf.format(new Date());
    }
}
