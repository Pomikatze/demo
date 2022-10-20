package com.example.demo.service.api;

import java.util.Optional;

/**
 * Интерфейс для кеширования выданных токенов.
 */
public interface TokenCacheService {

    /**
     * Получить token, null если state не представлен.
     *
     * @return state
     */
    Optional<String> getIfPresent(String state);

    /**
     * Сохранить token.
     */
    void put(String state, String token);

    /**
     * Удаляет элемент из кэша.
     *
     * @param state state
     */
    void invalidate(String state);
}
