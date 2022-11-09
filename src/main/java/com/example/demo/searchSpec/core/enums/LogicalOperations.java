package com.example.demo.searchSpec.core.enums;

/**
 * Логические операторы для соединения поисковых параметров.
 */
public enum LogicalOperations {

    AND("AND"),
    OR("OR");

    /**
     * Токен операции.
     */
    private final String token;

    LogicalOperations(String token) {
        this.token = token;
    }

    /**
     * Возвращает токен.
     *
     * @return  токен
     */
    public String getToken() {
        return token;
    }
}
