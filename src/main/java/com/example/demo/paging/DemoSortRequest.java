package com.example.demo.paging;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Sort;

/**
 * Класс для работы с сортировкой.
 */
@Schema(description = "Класс для работы с сортировкой")
@Data
public class DemoSortRequest {

    @Schema(description = "Наименование параметра", defaultValue = "id")
    private String property;
    @Schema(description = "Направление сортировки")
    private Sort.Direction direction = Sort.DEFAULT_DIRECTION;
    @Schema(description = "Порядок вывода null значений")
    private Sort.NullHandling nullHandling = Sort.NullHandling.NATIVE;

    /**
     * Получить объект Sort спринга.
     *
     * @return {@link Sort}
     */
    public Sort toSort() {
        if (property != null) {
            Sort.Order order = Sort.Order.by(property)
                    .with(nullHandling)
                    .with(direction);
            return Sort.by(order);
        } else {
            return Sort.unsorted();
        }
    }
}
