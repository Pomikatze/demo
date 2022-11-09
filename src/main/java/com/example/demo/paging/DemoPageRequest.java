package com.example.demo.paging;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.PageRequest;

/**
 * Класс для работы с пагинацией.
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Класс для работы с пагинацией")
@Data
public class DemoPageRequest extends DemoSortRequest {

    private static final int DEFAULT_PAGE_SIZE = 15;

    @Schema(description = "Номер страницы", defaultValue = "0")
    private int page;
    @Schema(description = "Размер страницы", defaultValue = "15")
    private int size = DEFAULT_PAGE_SIZE;

    /**
     * Получить объект PageRequest спринга.
     *
     * @return {@link PageRequest}
     */
    public PageRequest toPageRequest() {
        return PageRequest.of(page, size, toSort());
    }
}
