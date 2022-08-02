package com.example.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Вспомогательный класс для проверки тела HTTP ответа.
 */
public class ResponseBodyMatchers {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    /**
     * Проверка объектов.
     *
     * @param <T>
     */
    public <T> ResultMatcher isEmpty() {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            assertThat(json).isEmpty();
        };
    }

    /**
     * Проверка объектов.
     *
     * @param <T>
     */
    public <T> ResultMatcher containsObjectAsJson(
            Object expectedObject,
            Class<T> targetClass) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            T actualObject = OBJECT_MAPPER.readValue(json, targetClass);
            assertThat(actualObject).usingRecursiveComparison().isEqualTo(expectedObject);
        };
    }

    /**
     * Проверка объектов.
     *
     * @param <T>
     */
    public <T> ResultMatcher containsObjectAsJson(
            Object expectedObject,
            TypeReference<T> typeReference) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            T actualObject = OBJECT_MAPPER.readValue(json, typeReference);
            assertThat(actualObject).usingRecursiveComparison().isEqualTo(expectedObject);
        };
    }

    /**
     * @return @{@link ResponseBodyMatchers}
     */
    public static ResponseBodyMatchers responseBody() {
        return new ResponseBodyMatchers();
    }

}

