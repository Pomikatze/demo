package com.example.demo.searchSpec;

import com.example.demo.searchSpec.core.api.QueryParamFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Фильтр параметров поискового запроса.
 * Принимает в качестве фильта список атрибутов сущности.
 */
public class CommonQueryParamFilter implements QueryParamFilter {

    private final List<String> fields;
    private final boolean isAllowFilter;

    public CommonQueryParamFilter(List<String> fields, boolean isAllowFilter) {
        if (fields == null) {
            this.fields = new ArrayList<>();
        } else {
            this.fields = fields;
        }
        this.isAllowFilter = isAllowFilter;
    }

    public CommonQueryParamFilter(List<String> fields) {
        this(fields, true);
    }

    @Override
    public List<String> getFilter() {
        return Collections.unmodifiableList(fields);
    }

    @Override
    public boolean doFilter(String fieldName) {
        boolean has;
        if (this.fields.isEmpty()) {
            has = true;
        } else {
            has = fields.contains(fieldName.trim());
        }
        return isAllowFilter == has;
    }
}
