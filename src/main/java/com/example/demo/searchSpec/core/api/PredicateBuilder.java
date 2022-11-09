package com.example.demo.searchSpec.core.api;

import com.example.demo.searchSpec.core.enums.CompareOperations;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * Функциональный интерфейс построения предиката.
 *
 * @param <T>
 */
@FunctionalInterface
public interface PredicateBuilder<T> {

    /**
     * Сторитель предикатов.
     *
     * @param builder   builder
     * @param operation operation
     * @param from      from
     * @param key       key
     * @param value     value
     * @return          Predicate
     */
    Predicate build(CriteriaBuilder builder, CompareOperations operation,
                    From<T, T> from, String key, Object value, Class<?> fieldClass);
}
