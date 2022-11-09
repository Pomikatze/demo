package com.example.demo.searchSpec.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Помечает поля которые нужны для sql запроса, но их нет в entity.
 * Исползуется только для модуля data-search-spec.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface VirtualField {

    /**
     * field.
     */
    String field();
}
