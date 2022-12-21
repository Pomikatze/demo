package com.example.demo.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * Утилита для проверки объектов.
 */
@Slf4j
public final class CheckObjectUtil {

    /**
     * Проверить, что все поля объекта имеют значение null.
     *
     * @param model объект
     * @return true, если все поля объекта null, иначе false
     */
    @SneakyThrows
    public static <T> boolean isFieldsNull(T model) {
        Field[] fields = model.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(model);
            if (value != null) {
                return false;
            }
        }
        log.debug(String.format("Все поля объекта '%s' равны null", model));
        return true;
    }
}
