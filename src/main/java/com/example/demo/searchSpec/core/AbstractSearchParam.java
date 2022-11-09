package com.example.demo.searchSpec.core;

import com.example.demo.searchSpec.core.enums.CompareOperations;

import java.util.Map;

/**
 * Указывает что поисковые параметры поддерживают указание типа операции сравнения.
 */
public abstract class AbstractSearchParam {

    public static final String OPERATION_MAP_PROPERTY_NAME = "searchOperationMap";

    private Map<String, CompareOperations> searchOperationMap;

    /**
     * Возвращает мапу[атрибут, тип операции].
     *
     * @return {@link Map}<{@link String}, {@link CompareOperations}>
     */
    public Map<String, CompareOperations> getSearchOperationMap() {
        return searchOperationMap;
    }

    /**
     * Добавляет мапу[атрибут, тип операции].
     *
     * @param searchOperationMap {@link Map}<{@link String}, {@link CompareOperations}>
     */
    public void setSearchOperationMap(Map<String, CompareOperations> searchOperationMap) {
        this.searchOperationMap = searchOperationMap;
    }

    /**
     * Валидация атрибутов поиска.
     *
     * @return результат валидации {@link ValidationResult}
     */
    public ValidationResult validate() {
        return new ValidationResult(null);
    }

    /**
     * Определить маппинг операций к полям.
     *
     * @return {@link Map}<{@link String}, {@link CompareOperations}>
     */
    public Map<String, CompareOperations> operationMap() {
        return Map.of();
    }
}
