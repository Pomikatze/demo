package com.example.demo.utils.api;

import com.example.demo.dto.auth.YandexResponseDto;
import com.example.demo.entity.UserEntity;
import com.example.demo.jwt.JwtRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Утильный класс для авторизации.
 */
public interface AuthUtil {

    /**
     * Получить строковое значение из http-ответа.
     *
     * @param response {@link HttpResponse}
     * @return строковое значение
     */
    String getResultOfResponse(HttpResponse response) throws IOException;

    /**
     * Сохранить пользователя Яндекса в БД.
     *
     * @param dto {@link YandexResponseDto}
     * @return {@link UserEntity}
     */
    UserEntity saveYandexUser(YandexResponseDto dto, HttpClient client) throws IOException;

    /**
     *
     *
     * @param jwtRequest
     * @param request
     * @return
     */
    void createAuthToken(JwtRequest jwtRequest, HttpServletRequest request);
}
