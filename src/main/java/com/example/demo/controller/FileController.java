package com.example.demo.controller;

import com.example.demo.dto.FileWrapperResponse;
import com.example.demo.service.api.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

/**
 * Контроллер для работы с файлами.
 */
@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class FileController {

    private final FileStorageService service;

    /**
     * Сохранить файл в ФХ.
     *
     * @param file файл
     * @param principal пользователь
     * @return ссылка на файл
     */
    @Operation(summary = "Сохранить файл")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam MultipartFile file, Principal principal) {
        return service.uploadFile(file, principal);
    }

    /**
     * Скачать файл из ФХ.
     *
     * @param link ссылка на файл
     * @return файл
     */
    @Operation(summary = "Скачать файл из ФХ")
    @GetMapping(value = "/download/{link}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> download(@PathVariable String link) {
        FileWrapperResponse file = service.downloadFile(link);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName() + file.getExtension());
        return ResponseEntity.ok()
                .headers(headers)
                .body(file.getFile());
    }
}
