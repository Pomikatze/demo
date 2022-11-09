package com.example.demo.searchSpec;

import com.example.demo.searchSpec.core.AbstractSearchParam;
import com.example.demo.searchSpec.core.annotation.ListObject;
import com.example.demo.searchSpec.core.annotation.SearchObject;
import com.example.demo.searchSpec.core.annotation.VirtualField;
import com.example.demo.searchSpec.core.api.QueryParser;
import com.example.demo.searchSpec.core.enums.CompareOperations;
import com.example.demo.searchSpec.core.enums.GroupOperations;
import com.example.demo.searchSpec.core.enums.LogicalOperations;
import com.example.demo.searchSpec.exception.SearchProcessingException;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Формирует поисковый запрос на основе Dto.
 * Поисковые атрибуты соединяются логическим оператором AND.
 *
 * @param <T>
 */
public class DtoQueryParser<T> implements QueryParser<T> {

    /**
     * Операция сравнения по-умолчанию.
     */
    private static final CompareOperations DEFAULT_COMPARISON_OPERATION = CompareOperations.EQUALS;

    /**
     * Логическая операция по-умолчанию.
     */
    private static final String DEFAULT_LOGICAL_OPERATION = LogicalOperations.AND.getToken();

    /**
     * Настроки операции для атрибутов поиска.
     * Вид: имя атрибута, операция.
     */
    private Map<String, CompareOperations> operationMap = new HashMap<>();

    public DtoQueryParser() {
    }

    public DtoQueryParser(Map<String, CompareOperations> operationMap) {
        if (operationMap != null) {
            this.operationMap = operationMap;
        }
    }

    /**
     * Парсит поисковый запрос и возвращает в виде очереди.
     *
     * @param searchParam поисковый запрос
     * @return очередь
     */
    @Override
    public Deque<?> parse(T searchParam) {
        String searchQuery;

        Class<?> superclass = searchParam.getClass().getSuperclass();
        if (superclass.equals(AbstractSearchParam.class)) {
            Map<String, CompareOperations> searchOperationMap = ((AbstractSearchParam) searchParam).getSearchOperationMap();
            if (searchOperationMap != null) {
                mergeOperationMap(searchOperationMap);
            }
        }

        searchQuery = buildExpression(null, searchParam, DEFAULT_LOGICAL_OPERATION);

        CommonQueryParser<String> commonQueryParser = new CommonQueryParser<>();

        if (searchQuery.isEmpty()) {
            return commonQueryParser.parse(null);
        }

        return commonQueryParser.parse(searchQuery);
    }

    /**
     * Возвращает строку поиска исходя из параметров searchParam.
     *
     * @param parentFieldName имя атрибута родителя
     * @param searchParam     параметры поиска
     * @return строка поиска
     */
    private String buildExpression(String parentFieldName, Object searchParam, String logicOp) {
        StringBuilder searchQuery = new StringBuilder();
        Field[] declaredFields = searchParam.getClass().getDeclaredFields();

        for (Field field : declaredFields) {
            String fieldName = field.getName();

            if (fieldName.equalsIgnoreCase(AbstractSearchParam.OPERATION_MAP_PROPERTY_NAME)) {
                continue;
            }

            try {
                field.setAccessible(true);
                Object fieldValue = field.get(searchParam);

                if (fieldValue == null || fieldValue.toString().isEmpty()) {
                    continue;
                }

                ListObject listObject = field.getAnnotation(ListObject.class);

                SearchObject searchObject = field.getAnnotation(SearchObject.class);

                if (searchQuery.length() != 0) {
                    searchQuery.append(" " + logicOp + " ");
                }

                String tokenName = parentFieldName == null ? fieldName : parentFieldName + "." + fieldName;
                if (searchObject == null) {
                    VirtualField virtualField = field.getAnnotation(VirtualField.class);
                    if (virtualField != null) {
                        String virtualTokenName = parentFieldName == null ? virtualField.field() : parentFieldName + "." + virtualField.field();
                        searchQuery.append(virtualTokenName);
                    } else {
                        searchQuery.append(tokenName);
                    }
                    if (listObject != null) {
                        searchQuery.append(CompareOperations.IN.getToken());
                    } else {
                        searchQuery.append(getComparisonOperator(tokenName));
                    }
                    searchQuery.append("\"" + fieldValue + "\"");
                } else {
                    if (Collection.class.isAssignableFrom(field.getType())) {

                        Collection<String> entitiesExpression = ((Collection<?>) fieldValue).stream()
                                .map(obj -> new StringBuilder()
                                        .append(" ").append(GroupOperations.LEFT_BRACKET.getToken()).append(" ")
                                        .append(buildExpression(tokenName, obj, LogicalOperations.AND.getToken()))
                                        .append(" ").append(GroupOperations.RIGHT_BRACKET.getToken()).append(" ")
                                        .toString()
                                )
                                .collect(Collectors.toList());

                        String listExpression = String.join(" " + LogicalOperations.OR.getToken() + " ", entitiesExpression);

                        searchQuery
                                .append(" ").append(GroupOperations.LEFT_BRACKET.getToken()).append(" ")
                                .append(listExpression)
                                .append(" ").append(GroupOperations.RIGHT_BRACKET.getToken()).append(" ");
                    } else {
                        searchQuery.append(buildExpression(tokenName, fieldValue, logicOp));
                    }
                }
            } catch (IllegalAccessException ex) {
                throw new SearchProcessingException(ex.getMessage());
            }
        }
        return searchQuery.toString();
    }

    /**
     * Возвращает оператор сравнения для указанного атрибута исходя из мапы operationMap.
     * Если в operationMap значение отсутствует, то возвращается DEFAULT_COMPARISON_OPERATION.
     *
     * @param fieldName имя атрибута
     * @return операция сравнения
     */
    private String getComparisonOperator(String fieldName) {
        if (operationMap == null || operationMap.isEmpty()) {
            return DEFAULT_COMPARISON_OPERATION.getToken();
        }

        CompareOperations operation = operationMap.getOrDefault(fieldName, DEFAULT_COMPARISON_OPERATION);
        return operation.getToken() == null ? DEFAULT_COMPARISON_OPERATION.getToken() : operation.getToken();
    }

    /**
     * Производит слияние мапы операций operationMap с searchOperationMap.
     * В случает совпадения ключей приоритет имеет operationMap.
     *
     * @param searchOperationMap searchOperationMap
     */
    private void mergeOperationMap(Map<String, CompareOperations> searchOperationMap) {
        if (searchOperationMap != null) {
            for (Map.Entry<String, CompareOperations> entry : searchOperationMap.entrySet()) {
                if (!this.operationMap.containsKey(entry.getKey())) {
                    this.operationMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }
}
