package com.example.demo.searchSpec.core.enums;

/**
 * Операторы группировки для соединения поисковых параметров.
 */
public enum GroupOperations {

    LEFT_BRACKET("("),
    RIGHT_BRACKET(")");

    /**
     * Токен операции.
     */
    private final String token;

    GroupOperations(String token) {
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
