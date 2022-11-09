package com.example.demo.repositories;

import com.example.demo.entity.FileStorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileStorageRepository extends JpaRepository<FileStorageEntity, Long> {

    /**
     * Получить сущность файла из ФХ по токену.
     *
     * @param token токен
     * @return файл
     */
    FileStorageEntity getFileStorageEntityByToken(UUID token);
}
