package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Обёртка над файлом.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileWrapperResponse {

    @Schema(description = "Байтовое представление файла")
    private byte[] file;

    @Schema(description = "Наименование")
    private String name;

    @Schema(description = "Расширение")
    private String extension;
}
