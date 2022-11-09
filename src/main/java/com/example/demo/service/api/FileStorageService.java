package com.example.demo.service.api;

import com.example.demo.dto.FileWrapperResponse;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

/**
 * Сервис для работы с файлами.
 */
public interface FileStorageService {

    /**
     * Сохранить файл в ФХ.
     *
     * @param file файл
     * @param principal пользователь
     * @return ссылка на файл
     */
    String uploadFile(MultipartFile file, Principal principal);

    /**
     * Скачать файл из ФХ.
     *
     * @param link ссылка на файл
     * @return файл
     */
    FileWrapperResponse downloadFile(String link);
}
