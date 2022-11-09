package com.example.demo.searchSpec.core.api;

/**
 * Функциональный интерфейс преобразовывания value в типу fieldType.
 */
@FunctionalInterface
public interface TypeCaster {

    /**
     * Преобразовывает value в типу fieldType.
     *
     * @param fieldType fieldType
     * @param value     value
     * @return          Object
     */
    Object cast(Class<?> fieldType, Object value);
}
