package com.example.demo.searchSpec.exception;

/**
 * Исключение при внутренней ошибке поиска.
 */
public class SearchProcessingException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final String COMMON_MESSAGE = "Ошибка поиска: ";

    public SearchProcessingException(String message) {
        super(COMMON_MESSAGE + message);
    }

    public SearchProcessingException(String message, Throwable cause) {
        super(COMMON_MESSAGE + message, cause);
    }
}
