package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

/**
 *
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "file_storage")
public class FileStorageEntity {

    @Id
    @Column(name = "storage_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_storage_gen")
    @SequenceGenerator(name = "file_storage_gen", sequenceName = "file_storage_seq", allocationSize = 1)
    private Long id;

    @Column(name = "token")
    private UUID token;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "extension")
    private String extension;

    @Column(name = "user_name")
    private String userName;
}
