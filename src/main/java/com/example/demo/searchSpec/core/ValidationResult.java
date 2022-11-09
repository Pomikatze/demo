package com.example.demo.searchSpec.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Результат валидации поисковых полей.
 */
public class ValidationResult {

    private boolean valid = true;
    private Map<String, String> errors = new HashMap<>();

    public ValidationResult(Map<String, String> errors) {
        if (errors != null && !errors.isEmpty()) {
            valid = false;
            this.errors = errors;
        }
    }

    public boolean isValid() {
        return valid;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
