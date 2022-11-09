package com.example.demo.searchSpec.core;


import com.example.demo.searchSpec.core.enums.CompareOperations;

/**
 * Specification Search Criteria.
 */
public class SearchCriteria {

    /**
     * Ключ (название атрибута сущности)
     */
    private String key;

    /**
     * Операция (:,<,> и т.д.)
     */
    private CompareOperations operation;

    /**
     * Значение
     */
    private Object value;

    public SearchCriteria() {
    }

    public SearchCriteria(final String key, final CompareOperations operation, final Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SearchCriteria(String key, String operation, String value) {
        CompareOperations op = CompareOperations.getOperation(operation);
        this.key = key;
        this.operation = op;
        this.value = removeQuotes(value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public CompareOperations getOperation() {
        return operation;
    }

    public void setOperation(final CompareOperations operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(final Object value) {
        this.value = value;
    }

    private static String removeQuotes(String value) {
        return value.replaceAll("(^\"|\"$)", "").trim();
    }
}
