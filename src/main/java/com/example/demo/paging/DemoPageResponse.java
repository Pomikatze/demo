package com.example.demo.paging;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс ответа для работы с пагинацией.
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Класс ответа для работы с пагинацией")
public class DemoPageResponse<T> {

    @Schema(description = "Общее количество элементов")
    private long totalElements;
    @Schema(description = "Общее количество страниц")
    private int totalPages;
    @Schema(description = "Содержание страницы")
    private List<T> content;
}
