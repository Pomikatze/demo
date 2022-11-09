package com.example.demo.searchSpec.core.api;

import java.util.Deque;

/**
 * Фильтр параметров запроса.
 *
 * @param <T>
 */
public interface QueryParser<T> {

    /**
     * Парсит поисковый запрос и возвращает в виде очереди.
     *
     * @param searchParam   поисковый запрос
     * @return              очередь
     */
    Deque<?> parse(T searchParam);
}
