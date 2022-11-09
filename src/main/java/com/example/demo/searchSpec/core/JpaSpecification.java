package com.example.demo.searchSpec.core;

import com.example.demo.searchSpec.CommonPredicateBuilder;
import com.example.demo.searchSpec.CommonTypeCaster;
import com.example.demo.searchSpec.core.annotation.TypeReference;
import com.example.demo.searchSpec.core.api.PredicateBuilder;
import com.example.demo.searchSpec.core.api.TypeCaster;
import com.example.demo.searchSpec.exception.WrongSearchQueryException;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.Entity;
import javax.persistence.criteria.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Common Specification.
 *
 * @param <T> entity класс
 */
public class JpaSpecification<T> implements Specification<T> {

    private static final long serialVersionUID = 42L;

    /** Текущие тип сущности. */
    private final Class<T> clazz;
    /** Критерии поиска. */
    private final transient SearchCriteria criteria;
    /** Маппер типов полей запроса. */
    private final transient TypeCaster typeCaster;
    /** Билдер предиката. */
    private final transient PredicateBuilder<T> predicateBuilder;
    /** Список имен вложенных полей-сущностей. */
    private final Set<String> entityFields = new HashSet<>();

    /**
     * Конструктор.
     *
     * @param clazz             тип сущности
     * @param criteria          критерии поиска
     * @param predicateBuilder  билдер предиката
     * @param typeCaster        маппер типов полей запроса
     * @param fetchEntityFields список имен вложенных полей-сущностей (не обязательно)
     */
    public JpaSpecification(Class<T> clazz, final SearchCriteria criteria,
                            PredicateBuilder<T> predicateBuilder, TypeCaster typeCaster, Set<String> fetchEntityFields) {
        super();
        this.criteria = criteria;
        this.clazz = clazz;
        this.typeCaster = typeCaster;
        this.predicateBuilder = predicateBuilder;

        // Список полей сущностей (с аннотацией @Entity)
        if (fetchEntityFields != null && !fetchEntityFields.isEmpty()) {
            for (Field field : clazz.getDeclaredFields()) {
                if (fetchEntityFields.contains(field.getName()) && isEntity(field.getType())) {
                    this.entityFields.add(field.getName());
                }
            }
        }
    }

    public JpaSpecification(Class<T> clazz, final SearchCriteria criteria,
                            PredicateBuilder<T> predicateBuilder, TypeCaster typeCaster) {
        this(clazz, criteria, predicateBuilder, typeCaster, null);
    }

    public JpaSpecification(Class<T> clazz, final SearchCriteria criteria) {
        this(clazz, criteria, new CommonPredicateBuilder<>(), new CommonTypeCaster());
    }

    /**
     * Возвращает SpecSearchCriteria.
     *
     * @return SpecSearchCriteria
     */
    public SearchCriteria getCriteria() {
        return criteria;
    }

    /**
     * Возвращает предикат.
     * Поддерживает join дочерних сущностей (пример поля user.name).
     * Поиск по списку пока не поддерживается.
     *
     * @param root    root
     * @param query   query
     * @param builder builder
     * @return Predicate
     */
    @Override
    public Predicate toPredicate(final Root<T> root,
                                 final CriteriaQuery<?> query,
                                 final CriteriaBuilder builder) {
        String[] dotSplit = criteria.getKey().split("\\.");

        // Делаем так, чтобы в результате поиска все связанные сущности возвращались одним запросом
        for (String entityField : entityFields) {
            root.fetch(entityField, JoinType.LEFT);
        }
        entityFields.clear();

        if (dotSplit.length == 1) {
            Class<?> fieldClass = getFieldClass(clazz, criteria.getKey());
            Object value = typeCaster.cast(fieldClass, criteria.getValue());
            return predicateBuilder.build(builder, criteria.getOperation(), root, criteria.getKey(), value, fieldClass);
        } else {
            Join<T, T> join = null;
            String field;
            Class<?> fieldClass = clazz;

            for (int i = 0; i < dotSplit.length - 1; i++) {
                field = dotSplit[i];

                JoinType joinType = isCollection(fieldClass, field) ? JoinType.LEFT : JoinType.INNER;
                join = getJoinIfExistOrCreate(Objects.requireNonNullElse(join, root), field, joinType);

                fieldClass = getFieldClass(fieldClass, field);
            }

            field = dotSplit[dotSplit.length - 1];

            fieldClass = getFieldClass(fieldClass, field);

            Object value = typeCaster.cast(fieldClass, criteria.getValue());

            return predicateBuilder.build(builder, criteria.getOperation(), join, field, value, fieldClass);
        }
    }

    /**
     * Возвращает, является ли поле сущностью (с аннотацией @Entity).
     *
     * @param fieldClass тип поля
     * @return признак является ли сущностью
     */
    private boolean isEntity(Class<?> fieldClass) {
        Optional<Annotation> entity = Arrays.stream(fieldClass.getAnnotations())
                .filter(v -> v.annotationType().equals(Entity.class))
                .findFirst();
        if (entity.isPresent()) {
            return true;
        }
        return false;
    }

    /**
     * Возвращает класс поля fieldKey сущности fieldClass.
     *
     * @param fieldClass тип поля
     * @param fieldKey   имя поля
     * @return Class
     */
    private Class<?> getFieldClass(Class<?> fieldClass, String fieldKey) {
        try {
            Class<?> type;
            try {
                type = fieldClass.getDeclaredField(fieldKey).getType();
            } catch (NoSuchFieldException ex) {
                if (fieldClass.getSuperclass() == null) {
                    throw new NoSuchFieldException(ex.getLocalizedMessage());
                }
                type = fieldClass.getSuperclass().getDeclaredField(fieldKey).getType();
            }
            if (Collection.class.isAssignableFrom(type)) {
                TypeReference typeRef = fieldClass.getDeclaredField(fieldKey).getDeclaredAnnotation(TypeReference.class);
                if (typeRef == null) {
                    throw new WrongSearchQueryException(
                            String.format("Не известен тип обобщения поля %s в сущности %s", fieldKey, fieldClass));
                }
                return typeRef.type();
            }
            return type;
        } catch (NoSuchFieldException ex) {
            throw new WrongSearchQueryException(
                    String.format("Атрибут %s не найден в сущности %s", fieldKey, fieldClass), ex);
        }
    }

    /**
     * Возвращает, является ли поле коллекцией.
     *
     * @param fieldClass тип поля
     * @param field      имя поля
     * @return признак является ли коллекцией
     */
    private boolean isCollection(Class<?> fieldClass, String field) {
        return Arrays.stream(getFieldClass(fieldClass, field).getGenericInterfaces())
                .anyMatch(val -> val.getTypeName().startsWith("java.util.Collection"));
    }

    /**
     * Возвращает существующий join, либо создаёт новый.
     *
     * @param from     сущность для связи
     * @param field    поле, по которому происходит join
     * @param joinType join type
     * @return join
     */
    private Join<T, T> getJoinIfExistOrCreate(From<T, T> from, String field, JoinType joinType) {
        return from.getJoins().stream()
                .filter(join -> join.getAttribute().getName().equals(field))
                .findFirst()
                .map(join -> (Join<T, T>) join)
                .orElseGet(() -> from.join(field, joinType));
    }
}
