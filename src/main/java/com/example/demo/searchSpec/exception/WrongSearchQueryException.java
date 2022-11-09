package com.example.demo.searchSpec.exception;

/**
 * Исключение при неверном поисковойм запросе.
 */
public class WrongSearchQueryException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final String COMMON_MESSAGE = "Неверный поисковый зарос: ";

    public WrongSearchQueryException(String message) {
        super(COMMON_MESSAGE + message);
    }

    public WrongSearchQueryException(String message, Throwable cause) {
        super(COMMON_MESSAGE + message, cause);
    }
}
