package com.example.demo.searchSpec;


import com.example.demo.searchSpec.core.api.PredicateBuilder;
import com.example.demo.searchSpec.core.enums.CompareOperations;
import com.example.demo.searchSpec.exception.SearchProcessingException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса построения предиката.
 *
 * @param <T>
 */
public class CommonPredicateBuilder<T> implements PredicateBuilder<T> {

    /**
     * Возвращает предикат в соответсвии с типом операции.
     *
     * @param builder   builder
     * @param operation operation
     * @param from      from
     * @param key       key
     * @param value     value
     * @return          Predicate
     */
    @Override
    public Predicate build(CriteriaBuilder builder, CompareOperations operation,
                           From<T, T> from, String key, Object value, Class<?> fieldClass) {
        if (from == null) {
            throw new SearchProcessingException("Параметр \"from\" не может быть null");
        }
        if (key == null || key.isEmpty()) {
            throw new SearchProcessingException("Параметр \"key\" не может быть null или пустым");
        }

        switch (operation) {
            case EQUALS : return builder.equal(from.get(key), value);
            case EQUALS_IGNORE_CASE : return builder.equal(builder.lower(from.get(key)), value.toString().toLowerCase());
            case NOT_EQUALS: return builder.notEqual(from.get(key), value);
            case NOT_EQUALS_IGNORE_CASE: return builder.notEqual(builder.lower(from.get(key)), value.toString().toLowerCase());
            case GREATER : return builder.greaterThan(from.get(key), (Comparable) value);
            case GREATER_OR_EQUALS : return builder.greaterThanOrEqualTo(from.get(key), (Comparable) value);
            case LESS : return builder.lessThan(from.get(key), (Comparable) value);
            case LESS_OR_EQUALS : return builder.lessThanOrEqualTo(from.get(key), (Comparable) value);
            case STARTS : return builder.like(from.get(key), value + "%");
            case STARTS_IGNORE_CASE : return builder.like(builder.lower(from.get(key)), value.toString().toLowerCase() + "%");
            case ENDS : return builder.like(from.get(key), "%" + value);
            case ENDS_IGNORE_CASE : return builder.like(builder.lower(from.get(key)), "%" + value.toString().toLowerCase());
            case CONTAINS : return builder.like(from.get(key), "%" + value + "%");
            case CONTAINS_IGNORE_CASE : return builder.like(builder.lower(from.get(key)), "%" + value.toString().toLowerCase() + "%");
            case IN : return buildIn(fieldClass, value, builder, from, key);
            default : return null;
        }
    }

    private CriteriaBuilder.In<Object> buildIn(Class<?> fieldClass, Object value,
                                               CriteriaBuilder builder,
                                               From<T, T> from,
                                               String key) {

        List<?> values = List.of(value.toString()
            .replace(" ", "")
            .split(","));

        if (fieldClass.equals(Long.class)) {
            values = values.stream().map(x -> Long.parseLong(x.toString())).collect(Collectors.toList());
        }

        return builder.in(from.get(key)).value(values);
    }
}
