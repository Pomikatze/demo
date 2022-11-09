package com.example.demo.searchSpec.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Используется для определения типа в списке во время работы приложения.
 * Type erasure.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeReference {

    /**
     * Type of field.
     *
     * @return class of the generic field
     */
    Class<?> type();
}
