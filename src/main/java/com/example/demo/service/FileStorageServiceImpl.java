package com.example.demo.service;

import com.example.demo.dto.FileWrapperResponse;
import com.example.demo.entity.FileStorageEntity;
import com.example.demo.exception.DemoException;
import com.example.demo.repositories.FileStorageRepository;
import com.example.demo.service.api.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.Principal;
import java.util.UUID;

/**
 * Сервис для работы с файлами.
 */
@Service
@AllArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private static final String FILE_URI = "src/main/resources/fileStorage/";

    private final FileStorageRepository repository;

    @Override
    public String uploadFile(MultipartFile file, Principal principal) {
        FileStorageEntity entity = new FileStorageEntity();
        UUID token = UUID.randomUUID();
        String originalFilename = file.getOriginalFilename();

        assert originalFilename != null;
        StringBuilder reverse = new StringBuilder(originalFilename).reverse();
        int index = reverse.toString().indexOf('.') + 1;
        String extension = originalFilename.substring(originalFilename.length() - index);
        String fileName = originalFilename.substring(0, originalFilename.length() - index);
        String filePath = FILE_URI + token + extension;

        entity.setFileName(fileName);
        entity.setToken(token);
        entity.setExtension(extension);
        entity.setFilePath(filePath);

        if (principal != null) {
            entity.setUserName(principal.getName());
        }

        try (FileOutputStream outputStream = new FileOutputStream(filePath)){
            outputStream.write(file.getBytes());
        } catch (IOException e) {
            throw new DemoException("Ошибка получения данных файла");
        }

        repository.save(entity);

        return token.toString();
    }

    @Override
    public FileWrapperResponse downloadFile(String link) {
        FileWrapperResponse response = new FileWrapperResponse();
        FileStorageEntity entity = repository.getFileStorageEntityByToken(UUID.fromString(link));
        File file = new File(entity.getFilePath());

        response.setName(entity.getFileName());
        response.setExtension(entity.getExtension());

        try (FileInputStream inputStream = new FileInputStream(file)) {
            response.setFile(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new DemoException("Ошибка получения данных файла");
        }
        return response;
    }
}
