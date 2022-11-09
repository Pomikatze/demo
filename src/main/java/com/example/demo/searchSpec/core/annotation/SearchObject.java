package com.example.demo.searchSpec.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Используется для влючения поля класса-SearchParams в поиск.
 * Помеченное поле будет обрабатываться как вложенный объект.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SearchObject {

}
