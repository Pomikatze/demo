package com.example.demo.searchSpec.core;

import java.util.Set;

/**
 * Параметры поиска по спецификации.
 */
public class SpecParams {

    /**
     * Список полей сущностей, которые будут включены в select запрос результата поиска.
     */
    private final Set<String> fetchEntityFields;

    public SpecParams(Set<String> fetchEntityFields) {
        this.fetchEntityFields = fetchEntityFields;
    }

    public Set<String> getFetchEntityFields() {
        return fetchEntityFields;
    }
}
