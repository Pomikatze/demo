package com.example.demo.responseAdvice.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Обёртка над ответом сервера в API.
 *
 * @param <T> тип объекта, возвращаемого сервером
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper<T> {
    @Schema(description = "Данные об объекте")
    private T payload;
    @Schema(description = "Список сообщений об ошибках", nullable = true)
    private List<ErrorWrapper> errors;
    @Schema(description = "Список информационных сообщений", nullable = true)
    private List<InfoWrapper> infos;
    @Schema(description = "Данные о доступных прав", nullable = true)
    private AvailableRightsDto access;

    public ResponseWrapper() {
        // Без конструктора по умолчанию не проходят тесты.
    }

    public ResponseWrapper(Throwable throwable) {
        errors = new ArrayList<>(1);
        errors.add(new ErrorWrapper("", throwable.getMessage(), Arrays.toString(throwable.getStackTrace())));
    }

    public ResponseWrapper(T payload, AvailableRightsDto access) {
        this.payload = payload;
        this.access = access;
    }

    public ResponseWrapper(String info, T payload) {
        this.infos = new ArrayList<>(1);
        this.infos.add(new InfoWrapper(null, info));
        this.payload = payload;
    }

    public ResponseWrapper(T payload, String error) {
        this.payload = payload;
        errors = new ArrayList<>(1);
        errors.add(new ErrorWrapper(null, error, null));
    }

    public ResponseWrapper(T payload) {
        this.payload = payload;
        this.infos = new ArrayList<>();
    }

    public ResponseWrapper(T payload, Throwable throwable) {
        this.payload = payload;
        errors = new ArrayList<>(1);
        errors.add(new ErrorWrapper("", throwable.getMessage(), Arrays.toString(throwable.getStackTrace())));
    }

    public ResponseWrapper(T payload, List<ErrorWrapper> errors) {
        this.payload = payload;
        this.errors = errors;
    }

    public ResponseWrapper(T payload, String errorCode, String errorMsg, Throwable throwable) {
        this.payload = payload;
        errors = new ArrayList<>(1);
        errors.add(new ErrorWrapper(errorCode, errorMsg, Arrays.toString(throwable.getStackTrace())));
    }

    public ResponseWrapper(T payload, List<ErrorWrapper> errors, List<InfoWrapper> infos) {
        this.payload = payload;
        this.errors = errors;
        this.infos = infos;
    }

    /**
     * Метод для оборачивания ответа с дополнительной информацией.
     *
     * @param info     дополнительная информация об ответе
     * @param response ответ
     * @param <T>      тип ответа
     * @return новая обертка с дополнительной информацией
     */
    public static <T> ResponseWrapper<T> wrapWithInfo(String info, T response) {
        return new ResponseWrapper<>(info, response);
    }

    /**
     * Стандартный метод для оборачивания ответа.
     *
     * @param response ответ
     * @param <T>      тип ответа
     * @return новая обертка без дополнительной информации
     */
    public static <T> ResponseWrapper<T> wrap(T response) {
        return new ResponseWrapper<>(response);
    }

    /**
     * Метод для оборачивания ответа со списком ошибок.
     *
     * @param response ответ
     * @param errors   список ошибок
     * @param <T>      тип ответа
     * @return новая обертка со списком ошибок
     */
    public static <T> ResponseWrapper<T> wrap(T response, List<ErrorWrapper> errors) {
        return new ResponseWrapper<>(response, errors);
    }

    /**
     * Метод для оборачивания ответа со списком ошибок.
     *
     * @param response ответ
     * @param errors   список ошибок
     * @param <T>      тип ответа
     * @return новая обертка со списком ошибок
     */
    public static <T> ResponseWrapper<T> wrap(T response, List<ErrorWrapper> errors, List<InfoWrapper> infos) {
        return new ResponseWrapper<>(response, errors, infos);
    }

    /**
     * Метод для оборачивания ответа с отдельной ошибкой.
     *
     * @param response  ответ
     * @param throwable ошибка
     * @param <T>       тип ответа
     * @return новая обертка с ошибкой
     */
    public static <T> ResponseWrapper<T> wrap(T response, Throwable throwable) {
        return new ResponseWrapper<>(response, throwable);
    }

    /**
     * Метод для оборачивания ответа с отдельной ошибкой.
     *
     * @param response ответ
     * @param access   данные доступной информации
     * @param <T>      тип ответа
     * @return новая обертка с ошибкой
     */
    public static <T> ResponseWrapper<T> wrap(T response, AvailableRightsDto access) {
        return new ResponseWrapper<>(response, access);
    }

    /**
     * Метод для оборачивания ответа с сообщением об ошибке.
     *
     * @param response ответ
     * @param errorMsg сообщения об ошибке
     * @param <T>      тип ответа
     * @return новая обертка с сообщением об ошибке
     */
    public static <T> ResponseWrapper<T> wrap(T response, String errorMsg) {
        return new ResponseWrapper<>(response, errorMsg);
    }

    /**
     * Метод для оборачивания ответа с полной информацией об ошибке.
     *
     * @param response  ответ
     * @param errorCode код ошибки
     * @param errorMsg  сообщение об ошибке
     * @param throwable ошибка
     * @param <T>       тип ответа
     * @return новая обертка с полной информацией об ошибке
     */
    public static <T> ResponseWrapper<T> wrap(T response, String errorCode, String errorMsg, Throwable throwable) {
        return new ResponseWrapper<>(response, errorCode, errorMsg, throwable);
    }
}
