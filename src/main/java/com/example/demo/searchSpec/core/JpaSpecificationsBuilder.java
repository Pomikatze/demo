package com.example.demo.searchSpec.core;

import com.example.demo.searchSpec.CommonPredicateBuilder;
import com.example.demo.searchSpec.CommonTypeCaster;
import com.example.demo.searchSpec.core.api.PredicateBuilder;
import com.example.demo.searchSpec.core.api.TypeCaster;
import com.example.demo.searchSpec.core.enums.LogicalOperations;
import com.example.demo.searchSpec.exception.WrongSearchQueryException;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

/**
 * Common Specifications Builder.
 *
 * @param <T>   entity класс
 */
public class JpaSpecificationsBuilder<T> {

    protected final Class<T> clazz;
    protected final List<SearchCriteria> params;
    protected final SpecParams specParams;

    /**
     * Конструктор.
     *
     * @param clazz entity класс
     */
    public JpaSpecificationsBuilder(Class<T> clazz) {
        this.clazz = clazz;
        this.params = new ArrayList<>();
        this.specParams = null;
    }

    /**
     * Конструктор.
     *
     * @param clazz entity класс
     * @param specParams searchParams
     */
    public JpaSpecificationsBuilder(Class<T> clazz, SpecParams specParams) {
        this.clazz = clazz;
        this.params = new ArrayList<>();
        this.specParams = specParams;
    }

    /**
     * Build Specification.
     *
     * @param postFixedExprStack    postFixedExprStack
     * @param predicateBuilder      predicateBuilder
     * @param typeCaster            typeCaster
     * @return                      Specification
     */
    public Specification<T> build(Deque<?> postFixedExprStack, PredicateBuilder<T> predicateBuilder, TypeCaster typeCaster) {
        Deque<Specification<T>> specStack = new LinkedList<>();

        if (postFixedExprStack.isEmpty()) {
            return null;
        }

        Collections.reverse((List<?>) postFixedExprStack);

        while (!postFixedExprStack.isEmpty()) {
            Object mayBeOperand = postFixedExprStack.pop();

            if (!(mayBeOperand instanceof String)) {
                Set<String> fetchEntityFields = specParams == null ? null : specParams.getFetchEntityFields();
                JpaSpecification<T> specification = new JpaSpecification<>(this.clazz, (SearchCriteria) mayBeOperand,
                        predicateBuilder, typeCaster, fetchEntityFields);
                specStack.push(specification);
            } else {
                if (specStack.size() < 2) {
                    throw new WrongSearchQueryException("логические операторы AND и OR должны иметь 2 операнда");
                }

                Specification<T> operand1 = specStack.pop();
                Specification<T> operand2 = specStack.pop();
                if (mayBeOperand.equals(LogicalOperations.AND.getToken())) {
                    specStack.push(Specification.where(operand1)
                            .and(operand2));
                } else if (mayBeOperand.equals(LogicalOperations.OR.getToken())) {
                    specStack.push(Specification.where(operand1)
                            .or(operand2));
                }
            }
        }

        return specStack.pop();
    }

    /**
     * Build Specification.
     *
     * @param postFixedExprStack    postFixedExprStack
     * @return                      Specification
     */
    public Specification<T> build(Deque<?> postFixedExprStack) {
        return build(postFixedExprStack, new CommonPredicateBuilder<>(), new CommonTypeCaster());
    }
}
