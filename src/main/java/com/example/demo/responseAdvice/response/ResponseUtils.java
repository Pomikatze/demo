package com.example.demo.responseAdvice.response;

/**
 * Класс по созданию ответа с ошибкой.
 */
public class ResponseUtils {

    /**
     * Метод создает объект типа ErrorWrapper.
     * @param errorCode
     * @param errorMsg
     * @param errorDetail
     * @return {@link ErrorWrapper}
     */
    public static ErrorWrapper createErrorWrapper(String errorCode, String errorMsg, String errorDetail) {
        return new ErrorWrapper(errorCode, errorMsg, errorDetail);
    }
}
