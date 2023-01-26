package com.example.demo.responseAdvice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Данные о доступных правах.
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AvailableRightsDto {

    /**
     * Идентификаторы задач МУЗ.
     */
    @Schema(description = "Идентификаторы задач МУЗ")
    List<String> tasks;

    /**
     * Регионы.
     */
    @Schema(description = "Регионы")
    List<String> regions;
}
