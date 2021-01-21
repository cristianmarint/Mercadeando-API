package com.api.mercadeando.infrastructure.persistence.model;

///**
// * @author cristianmarint
// * @Date 2021-01-19 11:29
// */

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * Permite almacenar archivos sin duplicar con metadatos
 */
@Component
@ConfigurationProperties(prefix = "file")
@Entity
@Table(name = "file_storage")
@Getter
@Setter
public class FileStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long UserId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "document_type")
    @Enumerated(EnumType.STRING)
    private FileStorageDocumentType documentType;

    @Column(name = "document_format")
    private String documentFormat;

    @Column(name = "upload_dir")
    private String uploadDir;

    /**
     * Tamaño del archivo en bytes
     */
    @Column(name = "file_size")
    private long fileSize;
}
