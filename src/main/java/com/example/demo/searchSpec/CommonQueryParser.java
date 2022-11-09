package com.example.demo.searchSpec;


import com.example.demo.searchSpec.core.SearchCriteria;
import com.example.demo.searchSpec.core.api.QueryParamFilter;
import com.example.demo.searchSpec.core.api.QueryParser;
import com.example.demo.searchSpec.core.enums.CompareOperations;
import com.example.demo.searchSpec.core.enums.GroupOperations;
import com.example.demo.searchSpec.core.enums.LogicalOperations;
import com.example.demo.searchSpec.exception.WrongSearchQueryException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Парсит поисковый запрос и возвращает в виде очереди.
 * Вид запроса: [имя_атрибута][операция сравнения]["][значение]["] (можно соединять при помощи AND, OR).
 * Операции смотреть в SearchOperation.
 * Возможен поиск по частичному совпадению значения.
 *
 * @param <T>
 */
public class CommonQueryParser<T> implements QueryParser<T> {

    /**
     * Перечень операций сравнения
     */
    private static final Map<String, Operator> OPERATORS = Map.of(
            "AND", Operator.AND,
            "OR", Operator.OR,
            "or", Operator.OR,
            "and", Operator.AND
    );

    /**
     * Шаблон для проверки и парсинга строки запроса
     */
    private final Pattern specCriteriaRegex;

    /**
     * Фильтр поиска.
     */
    private final QueryParamFilter paramFilter;

    public CommonQueryParser() {
        this(null);
    }

    public CommonQueryParser(QueryParamFilter paramFilter) {
        this.paramFilter = paramFilter;

        String operations = String.join("|", CompareOperations.getOperations())
                .replaceAll("\\*", "\\\\*");
        String regex = String.format("^(?<key>[\\w\\.]+?)" +
                        "(?<op>%s)" +
                        "((?<value1>[\\w-]+?)" +
                        "|(\\\"(?<value2>.+?)\\\"))$",
                operations);
        specCriteriaRegex = Pattern.compile(regex);
    }

    private enum Operator {
        OR(1), AND(2);
        final int precedence;

        Operator(int p) {
            precedence = p;
        }
    }

    private static boolean isHigerPrecedenceOperator(String currOp, String prevOp) {
        return OPERATORS.containsKey(prevOp) && OPERATORS.get(prevOp).precedence >= OPERATORS.get(currOp).precedence;
    }

    /**
     * Парсит поисковый запрос и возвращает в виде очереди.
     *
     * @param searchParam   поисковый запрос
     * @return              очередь
     */
    @Override
    public Deque<?> parse(T searchParam) {

        if (searchParam == null) {
            return new LinkedList<>();
        }

        Deque<Object> output = new LinkedList<>();
        Deque<String> stack = new LinkedList<>();

        splitQueryString(searchParam.toString()).forEach(token -> {
            if (OPERATORS.containsKey(token)) {
                while (!stack.isEmpty() && isHigerPrecedenceOperator(token, stack.peek())) {
                    output.push(stack.pop().equalsIgnoreCase(LogicalOperations.OR.getToken()) ?
                            LogicalOperations.OR.getToken() : LogicalOperations.AND.getToken());
                }
                stack.push(token.equalsIgnoreCase(LogicalOperations.OR.getToken()) ?
                        LogicalOperations.OR.getToken() : LogicalOperations.AND.getToken());
            } else if (token.equals(GroupOperations.LEFT_BRACKET.getToken())) {
                stack.push(GroupOperations.LEFT_BRACKET.getToken());
            } else if (token.equals(GroupOperations.RIGHT_BRACKET.getToken())) {
                while (!stack.peek().equals(GroupOperations.LEFT_BRACKET.getToken())) {
                    output.push(stack.pop());
                }
                stack.pop();
            } else {
                Matcher matcher = specCriteriaRegex.matcher(token);
                int outputSize = output.size();

                while (matcher.find()) {
                    String key = matcher.group("key");

                    // фильтр по атрибуту поиска
                    if (this.paramFilter != null && !this.paramFilter.doFilter(key)) {
                        throw new WrongSearchQueryException(
                                String.format("поиск по атрибуту %s не поддерживается", key));
                    }

                    String operation = matcher.group("op");
                    String value = matcher.group("value1");

                    if (value == null) {
                        value = matcher.group("value2");
                    }

                    output.push(new SearchCriteria(key, operation, value));
                }

                if (output.size() == outputSize) {
                    throw new WrongSearchQueryException("неверный синтаксис");
                }
            }
        });

        while (!stack.isEmpty()) {
            output.push(stack.pop());
        }

        if (output.isEmpty()) {
            throw new WrongSearchQueryException("передан пустой фильтр");
        }

        return output;
    }

    /**
     * Разбивает строку запроса на части.
     *
     * @param queryString   queryString
     * @return              List
     */
    private List<String> splitQueryString(String queryString) {
        List<String> tokens = new ArrayList<>();

        boolean isInsideBrackets = false;

        String normalizedQueryString = queryString.replaceAll("[\\[\\]]", "").trim();
        for (int i = 0, k = 0; i < normalizedQueryString.length(); i++) {
            char ch = normalizedQueryString.charAt(i);
            if (ch == ' ' && !isInsideBrackets || i == normalizedQueryString.length() - 1) {
                String token = normalizedQueryString.substring(k, i + 1).trim();
                if (!token.isEmpty()) {
                    tokens.add(token);
                }
                k = i;
            } else if (ch == '"' && i != 0 && normalizedQueryString.charAt(i) != '\\') {
                isInsideBrackets = !isInsideBrackets;
            }
        }

        return tokens;
    }
}
