package com.example.demo.searchSpec;

import com.example.demo.searchSpec.core.api.TypeCaster;
import com.example.demo.searchSpec.exception.SearchProcessingException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * Преобразовывает value в типу fieldType.
 */
public class CommonTypeCaster implements TypeCaster {

    /**
     * Преобразовывает value в типу fieldType.
     * При необходимости сюда нужно добавлять конверторы.
     *
     * @param fieldType fieldType
     * @param value     value
     * @return          Object
     */
    @Override
    public Object cast(Class<?> fieldType, Object value) {
        try {
            if (fieldType.equals(LocalDate.class)) {
                return LocalDate.parse(value.toString());
            } else if (fieldType.equals(LocalDateTime.class)) {
                return LocalDateTime.parse(value.toString());
            } else if (fieldType.equals(Date.class)) {
                return new Date(Long.parseLong(value.toString()));
            } else if (fieldType.equals(OffsetDateTime.class)) {
                return OffsetDateTime.parse(value.toString());
            } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                return Boolean.parseBoolean(value.toString());
            } else if (fieldType.isEnum()) {
                return Enum.valueOf((Class) fieldType, value.toString());
            } else if (fieldType.equals(UUID.class)) {
                return UUID.fromString(value.toString());
            }
        } catch (Exception ex) {
            throw new SearchProcessingException(
                    String.format("Ошибка преобразования атрибута к типу %s. Значение: %s",
                            fieldType, value), ex);
        }
        return value;
    }
}
