package com.example.demo.searchSpec.core.api;

import java.util.List;

/**
 * Интерфейс фильтра параметров запроса.
 */
public interface QueryParamFilter {

    /**
     * Возвращает списов полей фильтра.
     *
     * @return  List
     */
    List<String> getFilter();

    /**
     * Возвращает резельтат фильтрации.
     *
     * @param fieldName fieldName
     * @return          boolean
     */
    boolean doFilter(String fieldName);
}
