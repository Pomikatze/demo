package com.example.demo.searchSpec.core.enums;

import java.util.Arrays;

/**
 * Операторы сравнения атрибутов.
 */
public enum CompareOperations {

    EQUALS(":"),
    EQUALS_IGNORE_CASE("@"),
    NOT_EQUALS("!"),
    NOT_EQUALS_IGNORE_CASE("!@"),
    GREATER(">"),
    GREATER_OR_EQUALS(">:"),
    LESS("<"),
    LESS_OR_EQUALS("<:"),
    STARTS("*:"),
    STARTS_IGNORE_CASE("*@"),
    ENDS(":*"),
    ENDS_IGNORE_CASE("@*"),
    CONTAINS("~"),
    CONTAINS_IGNORE_CASE("~@"),
    IN("-");

    /**
     * Токен операции.
     */
    private final String token;

    CompareOperations(String token) {
        this.token = token;
    }

    /**
     * Возвращает токен операции.
     *
     * @return  токен
     */
    public String getToken() {
        return token;
    }

    /**
     * Возвращает операцию по символу операции.
     *
     * @param input input
     * @return      SearchOperation
     */
    public static CompareOperations getOperation(final String input) {
        switch (input) {
            case ":": return EQUALS;
            case "@": return EQUALS_IGNORE_CASE;
            case "!": return NOT_EQUALS;
            case "!@": return NOT_EQUALS_IGNORE_CASE;
            case ">": return GREATER;
            case ">:": return GREATER_OR_EQUALS;
            case "<": return LESS;
            case "<:": return LESS_OR_EQUALS;
            case "*:": return STARTS;
            case "*@": return STARTS_IGNORE_CASE;
            case ":*": return ENDS;
            case "@*": return ENDS_IGNORE_CASE;
            case "~": return CONTAINS;
            case "~@": return CONTAINS_IGNORE_CASE;
            case "-": return IN;
            default: return null;
        }
    }

    /**
     * Возвращает массив токенов операций сравнения.
     *
     * @return  String[]
     */
    public static String[] getOperations() {
        return Arrays.stream(CompareOperations.values())
                .map(CompareOperations::getToken)
                .sorted((s1, s2) -> s2.length() - s1.length())
                .toArray(String[]::new);
    }
}
